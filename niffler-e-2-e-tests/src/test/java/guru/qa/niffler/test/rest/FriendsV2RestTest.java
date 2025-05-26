package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.pageable.RestResponsePage;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import guru.qa.niffler.service.impl.GatewayV2ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@RestTest
public class FriendsV2RestTest {

  @RegisterExtension
  private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

  private final GatewayV2ApiClient gatewayApiV2Client = new GatewayV2ApiClient();
  private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

  @ApiLogin
  @User(friends = 1, incomeInvitations = 1)
  @Test
  void friendsAndIncomeInvitationsListShouldBeReturned(UserJson user, @Token String token) {
    final UserJson expectedFriend = user.testData().friends().getFirst();
    final UserJson expectedInvitation = user.testData().incomeInvitations().getFirst();

    final RestResponsePage<UserJson> response = gatewayApiV2Client.allFriends(token, 0, 2, null, null);

    Assertions.assertEquals(2, response.getContent().size());

    final UserJson actualInvitation = response.getContent().getFirst();
    final UserJson actualFriend = response.getContent().getLast();

    Assertions.assertEquals(expectedFriend.id(), actualFriend.id());
    Assertions.assertEquals(expectedInvitation.id(), actualInvitation.id());
  }

  @ApiLogin
  @User(friends = 2, incomeInvitations = 2)
  @Test
  void friendsAndIncomeInvitationsListShouldBeFilteredAndReturned(UserJson user, @Token String token) {
    final UserJson expectedFriend = user.testData().friends().getFirst();

    final RestResponsePage<UserJson> response = gatewayApiV2Client.allFriends(token, 0, 4, null, expectedFriend.username());
    Assertions.assertEquals(1, response.getContent().size());

    final UserJson filteredFriend = response.getContent().getFirst();
    Assertions.assertEquals(expectedFriend.id(), filteredFriend.id());
  }

  @ApiLogin
  @User(friends = 1)
  @Test
  void friendshipShouldBeRemovable(UserJson user, @Token String token) {
    final UserJson friend = user.testData().friends().getFirst();

    gatewayApiClient.removeFriend(token, friend.username());
    final RestResponsePage<UserJson> response = gatewayApiV2Client.allFriends(token, 0, 4, null, null);

    Assertions.assertEquals(0, response.getContent().size());
  }
  @ApiLogin
  @User(incomeInvitations = 1)
  @Test
  void incomeInvitationShouldBeAcceptable(UserJson user, @Token String token) {
    final UserJson incomeUser = user.testData().incomeInvitations().getFirst();

    gatewayApiClient.acceptInvitation(token, incomeUser.username());
    final RestResponsePage<UserJson> response = gatewayApiV2Client.allFriends(token, 0, 4, null, null);

    Assertions.assertEquals(1, response.getContent().size());
    Assertions.assertEquals(incomeUser.id(), response.getContent().getFirst().id());
  }
}
