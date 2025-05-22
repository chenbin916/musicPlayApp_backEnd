package com.musicplayer.exception;

public class UserException extends BusinessException {
    public UserException(String message) {
        super("USER_ERROR", message);
    }

    public static UserException usernameAlreadyExists(String username) {
        return new UserException("Username '" + username + "' is already taken");
    }

    public static UserException emailAlreadyExists(String email) {
        return new UserException("Email '" + email + "' is already registered");
    }

    public static UserException invalidCredentials() {
        return new UserException("Invalid username or password");
    }

    public static UserException accountDisabled() {
        return new UserException("Account is disabled");
    }

    public static UserException accountLocked() {
        return new UserException("Account is locked");
    }
} 