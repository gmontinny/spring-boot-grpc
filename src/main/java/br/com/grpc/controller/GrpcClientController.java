package br.com.grpc.controller;

import br.com.grpc.client.UserGrpcClient;
import br.com.grpc.user.proto.ListUsersResponse;
import br.com.grpc.user.proto.UserResponse;
import br.com.grpc.user.proto.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grpc-client")
@RequiredArgsConstructor
public class GrpcClientController {

    private final UserGrpcClient grpcClient;

    @PostMapping("/users")
    public UserResponse createUser(@RequestParam String name, 
                                 @RequestParam String email, 
                                 @RequestParam int age,
                                 @RequestParam UserStatus status) {
        return grpcClient.createUser(name, email, age, status);
    }

    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable long id) {
        return grpcClient.getUser(id);
    }

    @GetMapping("/users")
    public ListUsersResponse listUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return grpcClient.listUsers(page, size);
    }

    @GetMapping("/users/stream/{status}")
    public String streamUsers(@PathVariable UserStatus status) {
        grpcClient.streamUsersByStatus(status);
        return "Check logs for streamed users";
    }
}