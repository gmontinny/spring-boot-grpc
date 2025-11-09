package br.com.grpc.client;

import br.com.grpc.user.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UserGrpcClient {

    private final ManagedChannel channel;
    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public UserGrpcClient() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.blockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserResponse createUser(String name, String email, int age, UserStatus status) {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setName(name)
                .setEmail(email)
                .setAge(age)
                .setStatus(status)
                .build();
        return blockingStub.createUser(request);
    }

    public UserResponse getUser(long id) {
        GetUserRequest request = GetUserRequest.newBuilder()
                .setId(id)
                .build();
        return blockingStub.getUser(request);
    }

    public ListUsersResponse listUsers(int page, int size) {
        ListUsersRequest request = ListUsersRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();
        return blockingStub.listUsers(request);
    }

    public void streamUsersByStatus(UserStatus status) {
        GetUsersByStatusRequest request = GetUsersByStatusRequest.newBuilder()
                .setStatus(status)
                .build();
        
        Iterator<UserResponse> users = blockingStub.getUsersByStatus(request);
        while (users.hasNext()) {
            UserResponse user = users.next();
            log.info("Received user: {} - {}", user.getId(), user.getName());
        }
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}