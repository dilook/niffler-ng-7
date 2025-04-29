package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.service.impl.UsersDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {

  static UsersClient getInstance() {
    return "api".equals(System.getProperty("client.impl"))
        ? new UsersApiClient()
        : new UsersDbClient();
  }

  @Nonnull
  @Step("Create user {0}")
  UserJson createUser(String username, String password);

  @Step("Add {1} income invitation(s) to user {0}")
  void addIncomeInvitation(UserJson targetUser, int count);

  @Step("Add {1} outcome invitation(s) to user {0}")
  void addOutcomeInvitation(UserJson targetUser, int count);

  @Step("Add {1} friend(s) to user {0}")
  void addFriend(UserJson targetUser, int count);
}
