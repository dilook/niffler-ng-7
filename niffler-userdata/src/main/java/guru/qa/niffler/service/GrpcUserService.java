package guru.qa.niffler.service;

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
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcUserService extends NifflerUserDataServiceGrpc.NifflerUserDataServiceImplBase {


    private final UserService userService;

    public GrpcUserService(UserService userRepository) {
        this.userService = userRepository;
    }

    @Override
    public void getCurrentUser(UserNameRequest request, StreamObserver<UserResponse> responseObserver) {
        UserJson currentUser = userService.getCurrentUser(request.getUsername());
        responseObserver.onNext(currentUser.toGrpcUserResponse());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
        super.getAllUsers(request, responseObserver);
    }

    @Override
    public void getAllUsersPaginated(AllUsersPaginatedRequest request, StreamObserver<AllUsersPaginatedResponse> responseObserver) {
        super.getAllUsersPaginated(request, responseObserver);
    }

    @Override
    public void updateUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        super.updateUser(request, responseObserver);
    }

    @Override
    public void getFriends(FriendsRequest request, StreamObserver<AllUsersResponse> responseObserver) {
        super.getFriends(request, responseObserver);
    }

    @Override
    public void getFriendsPaginated(FriendsPaginatedRequest request, StreamObserver<AllUsersPaginatedResponse> responseObserver) {
        super.getFriendsPaginated(request, responseObserver);
    }

    @Override
    public void removeFriend(FriendRequest request, StreamObserver<com.google.protobuf.Empty> responseObserver) {
        super.removeFriend(request, responseObserver);
    }

    @Override
    public void sendInvitation(FriendRequest request, StreamObserver<UserResponse> responseObserver) {
        super.sendInvitation(request, responseObserver);
    }

    @Override
    public void acceptInvitation(FriendRequest request, StreamObserver<UserResponse> responseObserver) {
        super.acceptInvitation(request, responseObserver);
    }

    @Override
    public void declineInvitation(FriendRequest request, StreamObserver<UserResponse> responseObserver) {
        super.declineInvitation(request, responseObserver);
    }
}
