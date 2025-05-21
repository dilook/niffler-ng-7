package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

public class BubblesInAnyOrderCondition extends WebElementCondition {
    protected BubblesInAnyOrderCondition(String name) {
        super(name);
    }

    @NotNull
    @Override
    public CheckResult check(Driver driver, WebElement element) {
        return CheckResult.accepted();
    }
}
