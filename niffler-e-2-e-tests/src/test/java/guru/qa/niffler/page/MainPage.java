package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statisticBlock = $("#stat");
  private final SelenideElement historySpendingBlock = $("#stat");
  private final SelenideElement profileBtn = $("button[aria-label='Menu']");

  MenuComponent menu = new MenuComponent();

  class MenuComponent {
    void selectMenuItem(String menuItem) {
      $(byText(menuItem)).click();
    }
  }

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public MainPage checkMainPageIsLoaded() {
    statisticBlock.shouldBe(visible);
    historySpendingBlock.shouldBe(visible);
    return this;
  }

  public ProfilePage goToProfile() {
    profileBtn.click();
    menu.selectMenuItem("Profile");
    return new ProfilePage();
  }
}
