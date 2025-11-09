package br.com.grpc.service;

import br.com.grpc.exception.InvalidUserDataException;
import br.com.grpc.exception.UserNotFoundException;
import br.com.grpc.model.User;
import br.com.grpc.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@email.com")
                .age(25)
                .status(User.UserStatus.ACTIVE)
                .build();
    }

    @Test
    void createUser_ValidUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        User result = userService.createUser(validUser);

        assertNotNull(result);
        assertEquals(validUser.getName(), result.getName());
        verify(userRepository).save(validUser);
    }

    @Test
    void createUser_InvalidEmail_ThrowsException() {
        validUser.setEmail("invalid-email");

        assertThrows(InvalidUserDataException.class, () -> userService.createUser(validUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_ExistingUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(validUser.getId(), result.getId());
    }

    @Test
    void getUserById_NonExistingUser_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void deleteUser_ExistingUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.deleteById(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_NonExistingUser_ThrowsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(any());
    }
}