package com.example.leave.services;

import com.example.leave.models.User;

public interface UserService {
    User createUser(User user);
    User findByUsername(String username);

    String login(User user);
}
