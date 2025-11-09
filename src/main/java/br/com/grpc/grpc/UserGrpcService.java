package br.com.grpc.grpc;

import br.com.grpc.exception.InvalidUserDataException;
import br.com.grpc.exception.UserNotFoundException;
import br.com.grpc.mapper.UserMapper;
import br.com.grpc.model.User;
import br.com.grpc.service.UserService;
import br.com.grpc.user.proto.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            log.info("gRPC createUser called for: {}", request.getName());
            User user = userMapper.toEntity(request);
            User createdUser = userService.createUser(user);
            UserResponse response = userMapper.toProto(createdUser);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (InvalidUserDataException e) {
            log.error("Invalid user data: {}", e.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("Error creating user", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            log.info("gRPC getUser called for id: {}", request.getId());
            User user = userService.getUserById(request.getId());
            UserResponse response = userMapper.toProto(user);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("Error getting user", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            log.info("gRPC updateUser called for id: {}", request.getId());
            User user = userMapper.toEntity(request);
            User updatedUser = userService.updateUser(user);
            UserResponse response = userMapper.toProto(updatedUser);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (InvalidUserDataException e) {
            log.error("Invalid user data: {}", e.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("Error updating user", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }

    @Override
    public void deleteUser(DeleteUserRequest request, StreamObserver<DeleteUserResponse> responseObserver) {
        try {
            log.info("gRPC deleteUser called for id: {}", request.getId());
            boolean deleted = userService.deleteUser(request.getId());
            
            DeleteUserResponse response = DeleteUserResponse.newBuilder()
                    .setSuccess(deleted)
                    .setMessage(deleted ? "User deleted successfully" : "Failed to delete user")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("Error deleting user", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }

    @Override
    public void listUsers(ListUsersRequest request, StreamObserver<ListUsersResponse> responseObserver) {
        try {
            log.info("gRPC listUsers called - page: {}, size: {}", request.getPage(), request.getSize());
            
            int page = Math.max(0, request.getPage());
            int size = request.getSize() > 0 ? request.getSize() : 10;
            
            List<User> users = userService.getAllUsers(page, size);
            long totalCount = userService.getTotalCount();
            
            ListUsersResponse.Builder responseBuilder = ListUsersResponse.newBuilder()
                    .setTotalCount((int) totalCount)
                    .setPage(page)
                    .setSize(size);
            
            users.forEach(user -> responseBuilder.addUsers(userMapper.toProto(user)));
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (InvalidUserDataException e) {
            log.error("Invalid pagination parameters: {}", e.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("Error listing users", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }

    @Override
    public void getUsersByStatus(GetUsersByStatusRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            log.info("gRPC getUsersByStatus called for status: {}", request.getStatus());
            User.UserStatus status = userMapper.toEntityStatus(request.getStatus());
            List<User> users = userService.getUsersByStatus(status);
            
            users.forEach(user -> {
                UserResponse response = userMapper.toProto(user);
                responseObserver.onNext(response);
            });
            
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error getting users by status", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }
}