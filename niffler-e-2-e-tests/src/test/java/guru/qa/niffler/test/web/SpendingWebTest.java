package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

@WebTest
public class SpendingWebTest {

  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  )
  @Test
  void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
    final String newDescription = "Обучение Niffler Next Generation";

    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getSpendingTable()
        .editSpending("Обучение Advanced 2.0")
        .setNewSpendingDescription(newDescription)
        .saveSpending();

    new MainPage().getSpendingTable()
        .checkTableContains(newDescription);
  }

  @User
  @Test
  void shouldAddNewSpending(UserJson user) {
    String category = "Friends";
    int amount = 100;
    Date currentDate = new Date();
    String description = RandomDataUtils.randomSentence(3);

    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .addSpendingPage()
        .setNewSpendingCategory(category)
        .setNewSpendingAmount(amount)
        .setNewSpendingDate(currentDate)
        .setNewSpendingDescription(description)
        .saveSpending()
        .checkAlertMessage("New spending is successfully created");

    new MainPage().getSpendingTable()
        .checkTableContains(description);
  }

  @User
  @Test
  void shouldNotAddSpendingWithEmptyCategory(UserJson user) {
    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .addSpendingPage()
        .setNewSpendingAmount(100)
        .setNewSpendingDate(new Date())
        .saveSpending()
        .checkFormErrorMessage("Please choose category");
  }

  @User
  @Test
  void shouldNotAddSpendingWithEmptyAmount(UserJson user) {
    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .addSpendingPage()
        .setNewSpendingCategory("Friends")
        .setNewSpendingDate(new Date())
        .saveSpending()
        .checkFormErrorMessage("Amount has to be not less then 0.01");
  }

  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  )
  @Test
  void deleteSpendingTest(UserJson user) {
    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getSpendingTable()
        .deleteSpending("Обучение Advanced 2.0")
        .checkTableSize(0);
  }


  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  )
  @ScreenShotTest("img/expected-stat.png")
  void checkStatComponentTest(UserJson user, BufferedImage expectedImage) throws IOException {
    Selenide.open(LoginPage.URL, LoginPage.class)
            .fillLoginPage(user.username(), user.testData().password())
            .submit(new MainPage())
            .getStatistic()
            .checkSpendingDiagram(expectedImage)
            .checkCategories("Обучение 79990 ₽");
  }

  @User(
          spendings = {
              @Spending(category = "Обучение", description = "Обучение Advanced 2.0", amount = 79990),
              @Spending(category = "Путешествия",description = "Мадагаскар", amount = 18000)
          }
  )
  @ScreenShotTest(value = "img/expected-delete-stat.png")
  void checkStatComponentAfterSpendingDeleteTest(UserJson user, BufferedImage expectedImage) throws IOException {
    Selenide.open(LoginPage.URL, LoginPage.class)
            .fillLoginPage(user.username(), user.testData().password())
            .submit(new MainPage())
            .getSpendingTable()
            .deleteSpending("Обучение Advanced 2.0");
    new MainPage().getStatistic()
            .checkSpendingDiagram(expectedImage)
            .checkCategories("Путешествия 18000 ₽");
  }

  @User(
          spendings = @Spending(category = "Обучение", description = "Обучение Advanced 2.0", amount = 79990)
  )
  @ScreenShotTest(value = "img/expected-update-stat.png")
  void checkStatComponentAfterUpdateSpendingTest(UserJson user, BufferedImage expectedImage) throws IOException {
    Selenide.open(LoginPage.URL, LoginPage.class)
            .fillLoginPage(user.username(), user.testData().password())
            .submit(new MainPage())
            .getSpendingTable()
            .editSpending("Обучение Advanced 2.0")
            .setNewSpendingAmount(25000)
            .saveSpending();
    new MainPage().getStatistic()
            .checkSpendingDiagram(expectedImage)
            .checkCategories("Обучение 25000 ₽");
  }

  @User(
          spendings = {
                  @Spending(category = "Обучение", description = "Обучение Advanced 2.0", amount = 79990),
                  @Spending(category = "Путешествия",description = "Мадагаскар", amount = 18000)
          }
  )
  @ScreenShotTest(value = "img/expected-archive-stat.png")
  void checkStatComponentWithArchivedCategoryTest(UserJson user, BufferedImage expectedImage) throws IOException {
    Selenide.open(LoginPage.URL, LoginPage.class)
            .fillLoginPage(user.username(), user.testData().password())
            .submit(new MainPage())
            .getHeader()
            .toProfilePage()
            .archiveCategory("Обучение");
    new MainPage().getHeader()
            .toMainPage()
            .getStatistic()
            .checkSpendingDiagram(expectedImage)
            .checkCategories("Archived 79990 ₽", "Путешествия 18000 ₽");
  }
}

