package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SpendingTable {

    @Getter
    private final SelenideElement self = $("#spendings");

    private final SelectField periodSelect = new SelectField(self.$("#period"));
    private final SelectField currencySelect = new SelectField(self.$("#currency"));
    private final ElementsCollection rows = self.$("tbody").$$("tr");
    private final SearchField searchField = new SearchField();
    private final SelenideElement deleteButton = self.$("#delete");

    public SpendingTable() {
        self.shouldBe(visible);
    }

    @Step("Select period {0}")
    @Nonnull
    public SpendingTable selectPeriod(DataFilterValues period) {
        periodSelect.selectByValue(period.getPeriod());
        return this;
    }

    @Step("Select currency {0}")
    @Nonnull
    public SpendingTable selectCurrency(CurrencyValues currency) {
        periodSelect.selectByValue(currency.name());
        return this;
    }

    @Step("Edit spending with description {0}")
    @Nonnull
    public EditSpendingPage editSpending(String spendingDescription) {
        searchSpendingByDescription(spendingDescription);
        rows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Find spending by description {0}")
    @Nonnull
    public SpendingTable searchSpendingByDescription(String spendingDescription) {
        searchField.search(spendingDescription);
        return this;
    }

    @Step("Delete spending with description {0}")
    @Nonnull
    public SpendingTable deleteSpending(String spendingDescription) {
        searchSpendingByDescription(spendingDescription);
        rows.find(text(spendingDescription)).$$("td").get(0).click();
        deleteButton.click();
        return this;
    }

    @Step("Check that table contains spending with descriptions {0}")
    @Nonnull
    public SpendingTable checkThatTableContainsSpending(String... spendingDescriptions) {
        for (String spendingDescription : spendingDescriptions) {
            searchSpendingByDescription(spendingDescription);
            rows.find(text(spendingDescription)).shouldBe(visible);
        }
        return this;
    }

    @Step("Check that table contains {0} spending(s)")
    @Nonnull
    public SpendingTable checkTableSize(int expectedSize) {
        rows.shouldHave(size(expectedSize));
        return this;
    }
}
