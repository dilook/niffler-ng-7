package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "duck",
      spendings = @Spending(
          category = "ЯЯЯ",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  )
  @Test
  void categoryDescriptionShouldBeChangedFromTable(SpendJson[] spend) {
    final String newDescription = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin("duck", "12345")
        .getSpendingTable().editSpending(spend[0].description())
        .setNewSpendingDescription(newDescription)
        .save();

    new MainPage().getSpendingTable().checkThatTableContainsSpending(newDescription);
  }

  @User
  @Test
  void addNewSpending(UserJson user) {
    String categoryDescription = RandomDataUtils.randomSentence(4);
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .getHeader()
            .addSpending()
            .fillAndSaveSpending(
                    5000.0,
                    RandomDataUtils.randomCategoryName(),
                    categoryDescription
            )
            .checkAlertMessage("New spending is successfully created")
            .getSpendingTable()
            .checkThatTableContainsSpending(categoryDescription);
  }
}

