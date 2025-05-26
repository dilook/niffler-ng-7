package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.GatewayApiClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

@RestTest
public class FriendsRestTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    private final AuthApiClient authApiClient = new AuthApiClient();
    private final UsersApiClient usersApiClient = new UsersApiClient();

    @ApiLogin
    @User(friends = 1, incomeInvitations = 1)
    @Test
    void friendsAndIncomeInvitationsListShouldBeReturned(UserJson user, @Token String token) {
        final UserJson expectedFriend = user.testData().friends().getFirst();
        final UserJson expectedInvitation = user.testData().incomeInvitations().getFirst();

        final List<UserJson> response = gatewayApiClient.allFriends(token, null);

        Assertions.assertEquals(2, response.size());

        final UserJson actualInvitation = response.getFirst();
        final UserJson actualFriend = response.getLast();

        Assertions.assertEquals(expectedFriend.id(), actualFriend.id());
        Assertions.assertEquals(expectedInvitation.id(), actualInvitation.id());
    }

    @ApiLogin
    @User(friends = 1)
    @Test
    void friendshipShouldBeRemovable(UserJson user, @Token String token) {
        final UserJson friend = user.testData().friends().getFirst();

        gatewayApiClient.removeFriend(token, friend.username());
        final List<UserJson> response = gatewayApiClient.allFriends(token, null);

        Assertions.assertEquals(0, response.size());
    }

    @ApiLogin
    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationShouldBeAcceptable(UserJson user, @Token String token) {
        final UserJson incomeUser = user.testData().incomeInvitations().getFirst();

        gatewayApiClient.acceptInvitation(token, incomeUser.username());
        final List<UserJson> response = gatewayApiClient.allFriends(token, null);
        Assertions.assertEquals(1, response.size());

        UserJson friend = response.getFirst();
        Assertions.assertEquals(incomeUser.id(), friend.id());
        Assertions.assertEquals(FriendshipStatus.FRIEND, friend.friendshipStatus());
    }

    @ApiLogin
    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationShouldBeDeclinable(UserJson user, @Token String token) {
        final UserJson incomeUser = user.testData().incomeInvitations().getFirst();

        gatewayApiClient.declineInvitation(token, incomeUser.username());
        final List<UserJson> response = gatewayApiClient.allFriends(token, null);
        Assertions.assertEquals(0, response.size());
    }

    @ApiLogin
    @User
    @Test
    void afterSendFriendshipRequestInvitationsShouldCreated(UserJson sourceUser, @Token String token) {
        String targetUsername = RandomDataUtils.randomUsername();
        UserJson targetUser = usersApiClient.createUser(targetUsername, "12345");

        gatewayApiClient.sendInvitation(token, targetUsername);

        final List<UserJson> actualOutcomeInvitations = gatewayApiClient.allUsers(token, targetUsername);
        Assertions.assertEquals(1, actualOutcomeInvitations.size());
        Assertions.assertEquals(targetUser.id(), actualOutcomeInvitations.getFirst().id());
        Assertions.assertEquals(FriendshipStatus.INVITE_SENT, actualOutcomeInvitations.getFirst().friendshipStatus());


        final List<UserJson> actualIncomeInvitations = usersApiClient.friends(targetUsername, sourceUser.username());
        Assertions.assertEquals(1, actualIncomeInvitations.size());
        Assertions.assertEquals(sourceUser.id(), actualIncomeInvitations.getFirst().id());
        Assertions.assertEquals(FriendshipStatus.INVITE_RECEIVED, actualIncomeInvitations.getFirst().friendshipStatus());
    }

}
