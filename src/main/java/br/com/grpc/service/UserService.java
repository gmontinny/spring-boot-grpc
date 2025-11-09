package br.com.grpc.service;

import br.com.grpc.exception.InvalidUserDataException;
import br.com.grpc.exception.UserNotFoundException;
import br.com.grpc.model.User;
import br.com.grpc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        log.info("Creating user: {}", user.getName());
        validateUser(user);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        log.info("Getting user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User updateUser(User user) {
        log.info("Updating user: {}", user.getId());
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        validateUser(user);
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        return userRepository.deleteById(id);
    }

    public List<User> getAllUsers(int page, int size) {
        log.info("Getting all users - page: {}, size: {}", page, size);
        if (page < 0 || size <= 0) {
            throw new InvalidUserDataException("Invalid pagination parameters");
        }
        return userRepository.findAllPaginated(page, size);
    }

    public long getTotalCount() {
        return userRepository.count();
    }

    public List<User> getUsersByStatus(User.UserStatus status) {
        log.info("Getting users by status: {}", status);
        return userRepository.findByStatus(status);
    }

    private void validateUser(User user) {
        if (!StringUtils.hasText(user.getName())) {
            throw new InvalidUserDataException("Name is required");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new InvalidUserDataException("Email is required");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new InvalidUserDataException("Invalid email format");
        }
        if (user.getAge() == null || user.getAge() < 0 || user.getAge() > 150) {
            throw new InvalidUserDataException("Age must be between 0 and 150");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}