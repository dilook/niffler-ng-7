package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.AllUsersPaginatedRequest;
import guru.qa.niffler.grpc.AllUsersPaginatedResponse;
import guru.qa.niffler.grpc.AllUsersRequest;
import guru.qa.niffler.grpc.AllUsersResponse;
import guru.qa.niffler.grpc.FriendRequest;
import guru.qa.niffler.grpc.FriendsPaginatedRequest;
import guru.qa.niffler.grpc.FriendsRequest;
import guru.qa.niffler.grpc.NifflerUserDataServiceGrpc;
import guru.qa.niffler.grpc.UserNameRequest;
import guru.qa.niffler.grpc.UserRequest;
import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserJsonBulk;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

@GrpcService
public class GrpcUserService extends NifflerUserDataServiceGrpc.NifflerUserDataServiceImplBase {


    private final UserService userService;

    public GrpcUserService(UserService userRepository) {
        this.userService = userRepository;
    }

    @Override
    public void getCurrentUser(UserNameRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserJson currentUser = userService.getCurrentUser(request.getUsername());
        responseObserver.onNext(currentUser.toGrpcUserResponse());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
        final List<UserJsonBulk> userJsonBulks = userService.allUsers(request.getUsername(), request.getSearchQuery());
        final List<UserResponse> allUsers = userJsonBulks.stream().map(UserJsonBulk::toGrpcUserResponse).toList();
        responseObserver.onNext(AllUsersResponse.newBuilder().addAllUsers(allUsers).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsersPaginated(AllUsersPaginatedRequest request, StreamObserver<AllUsersPaginatedResponse> responseObserver) {
        final Page<UserJsonBulk> userJsonBulks = userService.allUsers(request.getUsername(), PageRequest.of(request.getPage(), request.getSize()), request.getSearchQuery());
        final List<UserResponse> allUsers = userJsonBulks.stream().map(UserJsonBulk::toGrpcUserResponse).toList();
        responseObserver.onNext(AllUsersPaginatedResponse.newBuilder()
                .addAllEdges(allUsers)
                .setTotalElements(userJsonBulks.getTotalElements())
                .setTotalPages(userJsonBulks.getTotalPages())
                .setFirst(userJsonBulks.isFirst())
                .setLast(userJsonBulks.isLast())
                .setSize(userJsonBulks.getSize())
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserJson updated = userService.update(new UserJson(
                UUID.fromString(request.getId()),
                request.getUsername(),
                request.getFirstname(),
                request.getSurname(),
                request.getFullname(),
                request.getCurrency() == guru.qa.niffler.grpc.CurrencyValues.UNSPECIFIED ?
                        null : guru.qa.niffler.data.CurrencyValues.valueOf(request.getCurrency().name()),
                request.getPhoto(),
                request.getPhotoSmall(),
                request.getFriendshipStatus() == guru.qa.niffler.grpc.FriendshipStatus.UNSPECIFIED_STATUS ?
                        null: guru.qa.niffler.model.FriendshipStatus.valueOf(request.getFriendshipStatus().name())
        ));

        responseObserver.onNext(updated.toGrpcUserResponse());
        responseObserver.onCompleted();
    }

    @Override
    public void getFriends(FriendsRequest request, StreamObserver<AllUsersResponse> responseObserver) {
        final List<UserJsonBulk> userJsonBulks = userService.friends(request.getUsername(), request.getSearchQuery());
        final List<UserResponse> friends = userJsonBulks.stream().map(UserJsonBulk::toGrpcUserResponse).toList();
        responseObserver.onNext(AllUsersResponse.newBuilder().addAllUsers(friends).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getFriendsPaginated(FriendsPaginatedRequest request, StreamObserver<AllUsersPaginatedResponse> responseObserver) {
        final Page<UserJsonBulk> userJsonBulks = userService.friends(request.getUsername(), PageRequest.of(request.getPage(), request.getSize()), request.getSearchQuery());
        final List<UserResponse> friends = userJsonBulks.stream().map(UserJsonBulk::toGrpcUserResponse).toList();
        responseObserver.onNext(AllUsersPaginatedResponse.newBuilder()
                .addAllEdges(friends)
                .setTotalElements(userJsonBulks.getTotalElements())
                .setTotalPages(userJsonBulks.getTotalPages())
                .setFirst(userJsonBulks.isFirst())
                .setLast(userJsonBulks.isLast())
                .setSize(userJsonBulks.getSize())
                .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void removeFriend(FriendRequest request, StreamObserver<com.google.protobuf.Empty> responseObserver) {
        userService.removeFriend(request.getUsername(), request.getTargetUsername());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void sendInvitation(FriendRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserJson targetUserJson = userService.createFriendshipRequest(request.getUsername(), request.getTargetUsername());
        responseObserver.onNext(targetUserJson.toGrpcUserResponse());
        responseObserver.onCompleted();
    }

    @Override
    public void acceptInvitation(FriendRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserJson targetUserJson = userService.acceptFriendshipRequest(request.getUsername(), request.getTargetUsername());
        responseObserver.onNext(targetUserJson.toGrpcUserResponse());
        responseObserver.onCompleted();
    }

    @Override
    public void declineInvitation(FriendRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserJson targetUserJson = userService.declineFriendshipRequest(request.getUsername(), request.getTargetUsername());
        responseObserver.onNext(targetUserJson.toGrpcUserResponse());
        responseObserver.onCompleted();
    }
}
