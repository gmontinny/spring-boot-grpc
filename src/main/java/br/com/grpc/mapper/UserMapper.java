package br.com.grpc.mapper;

import br.com.grpc.model.User;
import br.com.grpc.user.proto.*;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class UserMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public User toEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .status(toEntityStatus(request.getStatus()))
                .build();
    }

    public User toEntity(UpdateUserRequest request) {
        return User.builder()
                .id(request.getId())
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .status(toEntityStatus(request.getStatus()))
                .build();
    }

    public UserResponse toProto(User user) {
        UserResponse.Builder builder = UserResponse.newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setAge(user.getAge())
                .setStatus(toProtoStatus(user.getStatus()));

        if (user.getCreatedAt() != null) {
            builder.setCreatedAt(user.getCreatedAt().format(FORMATTER));
        }
        if (user.getUpdatedAt() != null) {
            builder.setUpdatedAt(user.getUpdatedAt().format(FORMATTER));
        }

        return builder.build();
    }

    public User.UserStatus toEntityStatus(UserStatus protoStatus) {
        return switch (protoStatus) {
            case ACTIVE -> User.UserStatus.ACTIVE;
            case INACTIVE -> User.UserStatus.INACTIVE;
            case SUSPENDED -> User.UserStatus.SUSPENDED;
            default -> User.UserStatus.ACTIVE;
        };
    }

    private UserStatus toProtoStatus(User.UserStatus entityStatus) {
        return switch (entityStatus) {
            case ACTIVE -> UserStatus.ACTIVE;
            case INACTIVE -> UserStatus.INACTIVE;
            case SUSPENDED -> UserStatus.SUSPENDED;
        };
    }


}