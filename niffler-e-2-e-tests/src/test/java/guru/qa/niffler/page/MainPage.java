package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage {

    private final SelenideElement statComponent = $("#stat");
    private final Header header = new Header();
    private final SpendingTable spendingTable = new SpendingTable();

    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Nonnull
    public SpendingTable getSpendingTable() {
        return spendingTable;
    }

    @Nonnull
    public MainPage checkThatPageLoaded() {
        statComponent.should(visible).shouldHave(text("Statistics"));
        spendingTable.getSelf().shouldBe(visible).shouldHave(text("History of Spendings"));
        return this;
    }
}
