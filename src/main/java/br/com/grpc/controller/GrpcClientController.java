package br.com.grpc.controller;

import br.com.grpc.client.UserGrpcClient;
import br.com.grpc.user.proto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grpc-client")
@RequiredArgsConstructor
public class GrpcClientController {

    private final UserGrpcClient grpcClient;

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String name, 
                                 @RequestParam String email, 
                                 @RequestParam int age,
                                 @RequestParam UserStatus status) {
        try {
            UserResponse response = grpcClient.createUser(name, email, age, status);
            Map<String, Object> result = Map.of(
                "id", response.getId(),
                "name", response.getName(),
                "email", response.getEmail(),
                "age", response.getAge(),
                "status", response.getStatus().name(),
                "createdAt", response.getCreatedAt(),
                "updatedAt", response.getUpdatedAt()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "gRPC call failed: " + e.getMessage()));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable long id) {
        try {
            UserResponse response = grpcClient.getUser(id);
            Map<String, Object> result = Map.of(
                "id", response.getId(),
                "name", response.getName(),
                "email", response.getEmail(),
                "age", response.getAge(),
                "status", response.getStatus().name(),
                "createdAt", response.getCreatedAt(),
                "updatedAt", response.getUpdatedAt()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "gRPC call failed: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> listUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        try {
            ListUsersResponse response = grpcClient.listUsers(page, size);
            List<Map<String, Object>> users = response.getUsersList().stream()
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("name", user.getName());
                    userMap.put("email", user.getEmail());
                    userMap.put("age", user.getAge());
                    userMap.put("status", user.getStatus().name());
                    return userMap;
                })
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("users", users);
            result.put("totalCount", response.getTotalCount());
            result.put("page", response.getPage());
            result.put("size", response.getSize());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "gRPC call failed: " + e.getMessage()));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable long id,
                                                        @RequestParam String name,
                                                        @RequestParam String email,
                                                        @RequestParam int age,
                                                        @RequestParam UserStatus status) {
        try {
            UserResponse response = grpcClient.updateUser(id, name, email, age, status);
            Map<String, Object> result = Map.of(
                "id", response.getId(),
                "name", response.getName(),
                "email", response.getEmail(),
                "age", response.getAge(),
                "status", response.getStatus().name(),
                "createdAt", response.getCreatedAt(),
                "updatedAt", response.getUpdatedAt()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "gRPC call failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable long id) {
        try {
            DeleteUserResponse response = grpcClient.deleteUser(id);
            Map<String, Object> result = Map.of(
                "success", response.getSuccess(),
                "message", response.getMessage()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "gRPC call failed: " + e.getMessage()));
        }
    }

    @GetMapping("/users/stream/{status}")
    public String streamUsers(@PathVariable UserStatus status) {
        grpcClient.streamUsersByStatus(status);
        return "Check logs for streamed users";
    }
}