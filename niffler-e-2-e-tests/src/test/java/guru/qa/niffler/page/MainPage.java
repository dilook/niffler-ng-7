package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement profileBtn = $("button[aria-label='Menu']");

  MenuComponent menu = new MenuComponent();

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public FriendsPage goToFriends() {
    profileBtn.click();
    menu.selectMenuItem("Friends");
    return new FriendsPage();
  }

  public AllPeoplePage goToAllPeople() {
    profileBtn.click();
    menu.selectMenuItem("All People");
    return new AllPeoplePage();
  }
}
