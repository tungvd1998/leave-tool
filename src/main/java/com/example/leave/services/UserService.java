package com.example.leave.services;

import com.example.leave.models.User;
import com.example.leave.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    User createUser(User user);
    User findByUsername(String username);
    String login(UserDetails userDetails);
    String logout(String request);
    User get(Integer id);
    void save(User user);
    void delete(User user);

}
