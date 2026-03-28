package br.com.fiap.controller;

import br.com.fiap.services.UserService;
import com.fiap.user_service.users.api.UsersApi;
import com.fiap.user_service.users.dto.CreateUserRequestDTO;
import com.fiap.user_service.users.dto.PaginatedResultDTO;
import com.fiap.user_service.users.dto.UpdateUserRequestDTO;
import com.fiap.user_service.users.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    @NonNull
    private final UserService userService;

    @Override
    public ResponseEntity<UserDTO> createUser(@Valid final CreateUserRequestDTO body) {
        return ResponseEntity.ok(userService.createUser(body));
    }

    @Override
    public ResponseEntity<Void> deleteUserById(final Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PaginatedResultDTO> getAllUsers(Integer page, Integer size, String sort, String name, String email) {
        return ResponseEntity.ok(userService.getAllUsers(page, size, sort, name, email));
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Override
    public ResponseEntity<UserDTO> updateUserById(Long userId, UpdateUserRequestDTO body) {
        return ResponseEntity.ok(userService.updateUserById(userId, body));
    }
}
