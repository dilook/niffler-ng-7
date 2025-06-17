package guru.qa.niffler.test.grpc;

import guru.qa.niffler.grpc.AllUsersPaginatedResponse;
import guru.qa.niffler.grpc.AllUsersRequest;
import guru.qa.niffler.grpc.AllUsersResponse;
import guru.qa.niffler.grpc.FriendRequest;
import guru.qa.niffler.grpc.FriendsPaginatedRequest;
import guru.qa.niffler.grpc.FriendshipStatus;
import guru.qa.niffler.grpc.UserNameRequest;
import guru.qa.niffler.grpc.UserRequest;
import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserDataGrpcTest extends BaseGrpcTest {

    private final UsersClient usersClient = UsersClient.getInstance();

    @User
    @Test
    void getCurrentUserShouldReturnCorrectUser(UserJson user) {
        final UserResponse response = userDataBlockingStub.getCurrentUser(
                UserNameRequest.newBuilder()
                        .setUsername(user.username())
                        .build()
        );

        Assertions.assertEquals(user.username(), response.getUsername());
        Assertions.assertEquals(user.id().toString(), response.getId());
        Assertions.assertEquals("RUB", user.currency().name());
    }

    @Test
    @User(friends = 2, incomeInvitations = 1)
    void getAllUsersShouldReturnUsers(UserJson currentUser) {
        final AllUsersResponse response = userDataBlockingStub.getAllUsers(
                AllUsersRequest.newBuilder()
                        .setUsername(currentUser.username())
                        .build()
        );

        final List<UserResponse> allUsers = response.getUsersList();
        Assertions.assertFalse(allUsers.isEmpty());

        for (UserResponse user : allUsers) {
            Assertions.assertNotNull(user.getId());
            Assertions.assertNotNull(user.getUsername());
        }
    }

    @Test
    @User
    void updateUserShouldUpdateUserData(UserJson user) {
        final String image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAIAAAD/gAIDAAAANElEQVR4nO3BAQ0AAADCoPdPbQ43oAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAfgx1lAABqFDyOQAAAABJRU5ErkJggg==";
        String newFullname = RandomDataUtils.randomName();

        final UserResponse updatedUser = userDataBlockingStub.updateUser(
                UserRequest.newBuilder()
                        .setId(user.id().toString())
                        .setUsername(user.username())
                        .setFullname(newFullname)
                        .setCurrency(user.toGrpcUserResponse().getCurrency())
                        .setPhoto(image)
                        .build()
        );

        Assertions.assertEquals(user.id().toString(), updatedUser.getId());
        Assertions.assertEquals(user.username(), updatedUser.getUsername());
        Assertions.assertEquals(image, updatedUser.getPhoto());
        Assertions.assertNotNull(updatedUser.getPhotoSmall());
        Assertions.assertEquals(newFullname, updatedUser.getFullname());
    }

    @Test
    @User(friends = 5)
    void getFriendsPaginatedShouldReturnPageOfFriends(UserJson user) {
        int page = 0;
        int size = 3;

        final AllUsersPaginatedResponse paginatedResponse = userDataBlockingStub.getFriendsPaginated(
                FriendsPaginatedRequest.newBuilder()
                        .setUsername(user.username())
                        .setPage(page)
                        .setSize(size)
                        .build()
        );

        Assertions.assertEquals(5, paginatedResponse.getTotalElements(), "Total elements should match the number of friends");
        Assertions.assertEquals(2, paginatedResponse.getTotalPages(), "Total pages should be calculated correctly");
        Assertions.assertTrue(paginatedResponse.getFirst(), "Should be the first page");
        Assertions.assertFalse(paginatedResponse.getLast(), "Should not be the last page with 5 friends and page size 3");
        Assertions.assertEquals(size, paginatedResponse.getSize(), "Page size should match the requested size");
        Assertions.assertEquals(size, paginatedResponse.getEdgesCount(), "Number of friends in the page should match the page size");

        for (UserResponse friend : paginatedResponse.getEdgesList()) {
            Assertions.assertNotNull(friend.getId(), "Friend ID should not be null");
            Assertions.assertNotNull(friend.getUsername(), "Friend username should not be null");
            Assertions.assertEquals(FriendshipStatus.FRIEND, friend.getFriendshipStatus(), "Friendship status should be FRIEND");
        }
    }

    @Test
    @User(friends = 5)
    void getFriendsShouldReturnFilteredFriendsByUsername(UserJson user) {
        String searchQuery = user.testData().friends().getFirst().username().substring(0, 3);

        final AllUsersResponse response = userDataBlockingStub.getFriends(
                guru.qa.niffler.grpc.FriendsRequest.newBuilder()
                        .setUsername(user.username())
                        .setSearchQuery(searchQuery)
                        .build()
        );

        final List<UserResponse> filteredFriends = response.getUsersList();
        Assertions.assertFalse(filteredFriends.isEmpty());

        for (UserResponse friend : filteredFriends) {
            Assertions.assertNotNull(friend.getId());
            Assertions.assertNotNull(friend.getUsername());
            Assertions.assertTrue(friend.getUsername().contains(searchQuery));
            Assertions.assertEquals(FriendshipStatus.FRIEND, friend.getFriendshipStatus());
        }
    }

    @Test
    @User(friends = 1)
    void removeFriendShouldRemoveFriendship(UserJson user) {
        String targetUsername = user.testData().friends().getFirst().username();
        userDataBlockingStub.removeFriend(
                FriendRequest.newBuilder()
                        .setUsername(user.username())
                        .setTargetUsername(targetUsername)
                        .build()
        );

        final AllUsersResponse afterRemovalResponse = userDataBlockingStub.getFriends(
                guru.qa.niffler.grpc.FriendsRequest.newBuilder()
                        .setUsername(user.username())
                        .setSearchQuery(targetUsername)
                        .build()
        );

        Assertions.assertTrue(afterRemovalResponse.getUsersList().isEmpty(), "Friend should not be in the friends list after removal");
    }

    @Test
    @User(incomeInvitations = 1)
    void acceptInvitationShouldCreateFriendship(UserJson sourceUser) {
        String targetUsername = sourceUser.testData().incomeInvitations().getFirst().username();

        final UserResponse acceptResponse = userDataBlockingStub.acceptInvitation(
                FriendRequest.newBuilder()
                        .setUsername(sourceUser.username())
                        .setTargetUsername(targetUsername)
                        .build()
        );
        Assertions.assertEquals(targetUsername, acceptResponse.getUsername());
        Assertions.assertEquals(FriendshipStatus.FRIEND, acceptResponse.getFriendshipStatus());

        final AllUsersResponse friendsResponse = userDataBlockingStub.getFriends(
                guru.qa.niffler.grpc.FriendsRequest.newBuilder()
                        .setUsername(sourceUser.username())
                        .setSearchQuery(targetUsername)
                        .build()
        );
        boolean foundFriend = friendsResponse.getUsersList().stream()
                .anyMatch(ur -> targetUsername.equals(ur.getUsername()));
        Assertions.assertTrue(foundFriend, "Friend should be in the friends list");
    }

    @Test
    @User(incomeInvitations = 1)
    void declineInvitationShouldRejectFriendship(UserJson sourceUser) {
        String targetUsername = sourceUser.testData().incomeInvitations().getFirst().username();

        final UserResponse acceptResponse = userDataBlockingStub.declineInvitation(
                FriendRequest.newBuilder()
                        .setUsername(sourceUser.username())
                        .setTargetUsername(targetUsername)
                        .build()
        );
        Assertions.assertEquals(targetUsername, acceptResponse.getUsername());
        Assertions.assertEquals(FriendshipStatus.UNSPECIFIED_STATUS, acceptResponse.getFriendshipStatus());

        final AllUsersResponse friendsResponse = userDataBlockingStub.getFriends(
                guru.qa.niffler.grpc.FriendsRequest.newBuilder()
                        .setUsername(sourceUser.username())
                        .setSearchQuery(targetUsername)
                        .build()
        );
        boolean foundFriend = friendsResponse.getUsersList().stream()
                .anyMatch(ur -> targetUsername.equals(ur.getUsername()));
        Assertions.assertFalse(foundFriend, "Friend should not be in the friends list");
    }

    @Test
    @User
    void sendInvitationShouldCreateInvite(UserJson sourceUser) {
        String targetUsername = RandomDataUtils.randomUsername();
        usersClient.createUser(targetUsername, "12345");

        final UserResponse invitationResponse = userDataBlockingStub.sendInvitation(
                FriendRequest.newBuilder()
                        .setUsername(sourceUser.username())
                        .setTargetUsername(targetUsername)
                        .build()
        );

        Assertions.assertEquals(targetUsername, invitationResponse.getUsername());
        Assertions.assertEquals(FriendshipStatus.INVITE_SENT, invitationResponse.getFriendshipStatus());
    }
}
