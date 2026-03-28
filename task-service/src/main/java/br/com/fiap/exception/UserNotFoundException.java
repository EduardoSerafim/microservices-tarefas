package br.com.fiap.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final long userId) {
        super("User with ID " + userId + " not found");
    }

}
