package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {

  public static final String caseName = "Niffler backend logs";

  @SneakyThrows
  @Override
  public void afterSuite() {
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    final String caseId = UUID.randomUUID().toString();
    allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
    allureLifecycle.startTestCase(caseId);

    attachLog(allureLifecycle, "auth");
    attachLog(allureLifecycle, "currency");
    attachLog(allureLifecycle, "gateway");
    attachLog(allureLifecycle, "spend");
    attachLog(allureLifecycle, "userdata");

    allureLifecycle.stopTestCase(caseId);
    allureLifecycle.writeTestCase(caseId);
  }

  private static void attachLog(AllureLifecycle allureLifecycle, String serviceName) throws IOException {
    allureLifecycle.addAttachment(
        "Niffler-" + serviceName + " log",
        "text/html",
        ".log",
        Files.newInputStream(
            Path.of(String.format("./logs/niffler-%s/app.log", serviceName))
        )
    );
  }
}
