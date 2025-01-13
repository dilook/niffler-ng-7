package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.spendings())) {
            Spending spendAnno = userAnno.spendings()[0];
            SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                    null,
                    spendAnno.category(),
                    userAnno.username(),
                    false
                ),
                CurrencyValues.RUB,
                spendAnno.amount(),
                spendAnno.description(),
                userAnno.username()
            );
            context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                spendDbClient.createSpend(spend)
            );
          }
        });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    SpendJson spend = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
    if (spend != null ) {
      spendDbClient.deleteSpend(spend);
      spendDbClient.deleteCategory(spend.category());
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
  }

}
