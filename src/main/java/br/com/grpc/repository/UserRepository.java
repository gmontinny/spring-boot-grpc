package br.com.grpc.repository;

import br.com.grpc.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class UserRepository {
    
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public List<User> findByStatus(User.UserStatus status) {
        return users.values().stream()
                .filter(user -> user.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return users.remove(id) != null;
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    public long count() {
        return users.size();
    }

    public List<User> findAllPaginated(int page, int size) {
        return users.values().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
}