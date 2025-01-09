package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  public record StaticUser(
          String username,
          String password,
          String friend,
          String income,
          String outcome) {

  }

  private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

  static {
    EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
    WITH_FRIEND_USERS.add(new StaticUser("shmel", "12345", "duck", null, null));
    WITH_INCOME_REQUEST_USERS.add(new StaticUser("bug   ", "12345", null, "dima", null));
    WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("dima", "12345", null, null, "bug"));
  }

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface UserType {
    Type value() default Type.EMPTY;

    enum Type {
        EMPTY,
        WITH_FRIEND,
        WITH_INCOME_REQUEST,
        WITH_OUTCOME_REQUEST
    }
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
        .forEach(p -> {
          Optional<StaticUser> user = Optional.empty();
          StopWatch sw = StopWatch.createStarted();
          while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
              user = switch (p.getAnnotation(UserType.class).value()) {
                  case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
                  case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
                  case WITH_INCOME_REQUEST -> Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                  case WITH_OUTCOME_REQUEST -> Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
              };
          }
          user.ifPresentOrElse(u ->
                          ((Map<Parameter, StaticUser>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
                                  context.getUniqueId(),
                                  key -> new HashMap<>()
                          )).put(p, u),
              () -> {
                throw new IllegalStateException("Can`t obtain user after 30s.");
              }
          );
        });
      Allure.getLifecycle().updateTestCase(testCase ->
              testCase.setStart(new Date().getTime())
      );
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    Map<Parameter, StaticUser> map = context.getStore(NAMESPACE).get(
        context.getUniqueId(),
        Map.class
    );

    for (Map.Entry<Parameter, StaticUser> entry : map.entrySet()) {
        switch (entry.getKey().getAnnotation(UserType.class).value()) {
            case EMPTY -> EMPTY_USERS.add(entry.getValue());
            case WITH_FRIEND -> WITH_FRIEND_USERS.add(entry.getValue());
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(entry.getValue());
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(entry.getValue());
        }
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return ((Map<Parameter, StaticUser>) extensionContext.getStore(NAMESPACE)
            .get(extensionContext.getUniqueId(), Map.class))
            .get(parameterContext.getParameter());
  }
}
