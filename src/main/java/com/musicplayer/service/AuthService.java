package com.musicplayer.service;

import com.musicplayer.model.User;

public interface AuthService {
    String register(User user);
    String login(String username, String password);
    void logout(String token);
} 