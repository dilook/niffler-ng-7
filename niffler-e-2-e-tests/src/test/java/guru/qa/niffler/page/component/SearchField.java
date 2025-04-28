package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField {

    private final SelenideElement self = $("input[aria-label='search']");
    private final SelenideElement clearButton = $("#input-clear");

    @Nonnull
    @Step("Search '{0}'")
    public SearchField search(String text) {
        self.setValue(text).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Clear search field")
    public SearchField clearByButton() {
        if (self.is(not(empty))) {
            clearButton.click();
        }
        return this;
    }
}

