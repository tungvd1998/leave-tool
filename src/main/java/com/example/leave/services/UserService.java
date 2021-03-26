package com.example.leave.services;

import com.example.leave.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User findByUsername(String username);
    String loginUser(UserDetails userDetails);
    User getByIdUser(Integer id);
    void saveUser(User user);
    void deleteUser(User user);
    List<User> listAllUser();

}
