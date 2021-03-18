package com.example.leave.services;

import com.example.leave.models.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    User createUser(User user);
    User findByUsername(String username);

    String login(UserDetails userDetails);
}
