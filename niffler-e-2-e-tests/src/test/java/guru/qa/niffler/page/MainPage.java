package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement deleteBtn = $("#delete");


    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$("td", 5).click();

        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).shouldBe(visible);
    }

    public void checkThatTableDoesntContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(not(exist));
    }

    public void deleteSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$("td", 0).click();
        tableRows.find(text(spendingDescription)).shouldHave(attribute("aria-checked", "true"));

        deleteBtn.click();
        new ModalYesNoPage().delete();
    }
}
