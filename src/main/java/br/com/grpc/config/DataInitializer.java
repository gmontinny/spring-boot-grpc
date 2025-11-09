package br.com.grpc.config;

import br.com.grpc.model.User;
import br.com.grpc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        log.info("Initializing sample data...");

        User user1 = User.builder()
                .name("João Silva")
                .email("joao.silva@email.com")
                .age(30)
                .status(User.UserStatus.ACTIVE)
                .build();

        User user2 = User.builder()
                .name("Maria Santos")
                .email("maria.santos@email.com")
                .age(25)
                .status(User.UserStatus.ACTIVE)
                .build();

        User user3 = User.builder()
                .name("Pedro Oliveira")
                .email("pedro.oliveira@email.com")
                .age(35)
                .status(User.UserStatus.INACTIVE)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        log.info("Sample data initialized successfully. Total users: {}", userRepository.count());
    }
}