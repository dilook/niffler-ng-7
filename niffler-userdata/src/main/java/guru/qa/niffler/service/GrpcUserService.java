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

public class GrpcUserService extends NifflerUserDataServiceGrpc.NifflerUserDataServiceImplBase {
    @Override
    public void getCurrentUser(UserNameRequest request, io.grpc.stub.StreamObserver<UserResponse> responseObserver) {
        super.getCurrentUser(request, responseObserver);
    }

    @Override
    public void getAllUsers(AllUsersRequest request, io.grpc.stub.StreamObserver<AllUsersResponse> responseObserver) {
        super.getAllUsers(request, responseObserver);
    }

    @Override
    public void getAllUsersPaginated(AllUsersPaginatedRequest request,
                                     io.grpc.stub.StreamObserver<AllUsersPaginatedResponse> responseObserver) {
        super.getAllUsersPaginated(request, responseObserver);
    }

    @Override
    public void updateUser(UserRequest request, io.grpc.stub.StreamObserver<UserResponse> responseObserver) {
        super.updateUser(request, responseObserver);
    }

    @Override
    public void getFriends(FriendsRequest request, io.grpc.stub.StreamObserver<AllUsersResponse> responseObserver) {
        super.getFriends(request, responseObserver);
    }

    @Override
    public void getFriendsPaginated(FriendsPaginatedRequest request,
                                    io.grpc.stub.StreamObserver<AllUsersPaginatedResponse> responseObserver) {
        super.getFriendsPaginated(request, responseObserver);
    }

    @Override
    public void removeFriend(FriendRequest request, io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
        super.removeFriend(request, responseObserver);
    }

    @Override
    public void sendInvitation(FriendRequest request, io.grpc.stub.StreamObserver<UserResponse> responseObserver) {
        super.sendInvitation(request, responseObserver);
    }

    @Override
    public void acceptInvitation(FriendRequest request, io.grpc.stub.StreamObserver<UserResponse> responseObserver) {
        super.acceptInvitation(request, responseObserver);
    }

    @Override
    public void declineInvitation(FriendRequest request, io.grpc.stub.StreamObserver<UserResponse> responseObserver) {
        super.declineInvitation(request, responseObserver);
    }
}
