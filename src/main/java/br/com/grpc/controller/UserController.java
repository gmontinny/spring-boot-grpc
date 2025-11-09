package br.com.grpc.controller;

import br.com.grpc.model.User;
import br.com.grpc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        log.info("REST: Creating user: {}", request.getName());
        
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .status(User.UserStatus.valueOf(request.getStatus().toUpperCase()))
                .build();
        
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.info("REST: Getting user by id: {}", id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        log.info("REST: Updating user: {}", id);
        
        User user = User.builder()
                .id(id)
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .status(User.UserStatus.valueOf(request.getStatus().toUpperCase()))
                .build();
        
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        log.info("REST: Deleting user: {}", id);
        boolean deleted = userService.deleteUser(id);
        
        Map<String, Object> response = Map.of(
                "success", deleted,
                "message", deleted ? "User deleted successfully" : "Failed to delete user"
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<User>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("REST: Listing users - page: {}, size: {}", page, size);
        
        List<User> users = userService.getAllUsers(page, size);
        long totalCount = userService.getTotalCount();
        
        PagedResponse<User> response = new PagedResponse<>(users, totalCount, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable String status) {
        log.info("REST: Getting users by status: {}", status);
        
        User.UserStatus userStatus = User.UserStatus.valueOf(status.toUpperCase());
        List<User> users = userService.getUsersByStatus(userStatus);
        
        return ResponseEntity.ok(users);
    }

    // DTOs para REST API
    public static class CreateUserRequest {
        private String name;
        private String email;
        private Integer age;
        private String status;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class UpdateUserRequest {
        private String name;
        private String email;
        private Integer age;
        private String status;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class PagedResponse<T> {
        private List<T> content;
        private long totalElements;
        private int page;
        private int size;

        public PagedResponse(List<T> content, long totalElements, int page, int size) {
            this.content = content;
            this.totalElements = totalElements;
            this.page = page;
            this.size = size;
        }

        // Getters e Setters
        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
    }
}