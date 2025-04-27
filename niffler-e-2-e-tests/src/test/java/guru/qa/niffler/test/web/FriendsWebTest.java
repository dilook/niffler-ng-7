package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {

  private static final Config CFG = Config.getInstance();

  @User(friends = 11)
  @Test
  void friendShouldBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin(user.username(), user.testData().password())
        .checkThatPageLoaded()
        .getHeader()
        .toFriendsPage()
        .checkExistingFriends(user.testData().getFriendsNames());
  }

  @User
  @Test
  void friendsTableShouldBeEmptyForNewUser(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin(user.username(), user.testData().password())
        .checkThatPageLoaded()
        .getHeader()
        .toFriendsPage()
        .checkNoExistingFriends();
  }

  @User(incomeInvitations = 11)
  @Test
  void incomeInvitationBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin(user.username(), user.testData().password())
        .checkThatPageLoaded()
        .getHeader()
        .toFriendsPage()
        .checkExistingInvitations(user.testData().getIncomeInvitationsNames());
  }

  @User(outcomeInvitations = 11)
  @Test
  void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin(user.username(), user.testData().password())
        .checkThatPageLoaded()
        .getHeader()
        .toPeoplePage()
        .checkInvitationSentToUser(user.testData().getOutcomeInvitationsNames());
  }
}
