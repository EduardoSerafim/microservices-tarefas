package br.com.fiap.utils;

import br.com.fiap.model.User;
import com.fiap.user_service.users.dto.CreateUserRequestDTO;
import com.fiap.user_service.users.dto.UserDTO;

public class UserMapper {

    private UserMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static User toEntity(final CreateUserRequestDTO entity) {
        return new User(entity.getName(), entity.getEmail());
    }

    public static UserDTO toDTO(final User user) {
        return new UserDTO()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail());
    }
}
