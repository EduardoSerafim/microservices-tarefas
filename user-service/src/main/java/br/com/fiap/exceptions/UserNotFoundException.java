package br.com.fiap.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final Long userId) {
        super("User not found: " + userId);
    }
}
