package com.example.leave.services.Impl;

import com.example.leave.models.User;
import com.example.leave.repositories.UserRepository;
import com.example.leave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setCreated(new Date());
        return userRepository.save(user);
    }
    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        User userPrincipal = new User();
        if (null != user) {
            Set<String> authorities = new HashSet<>();
            userPrincipal.setUsername(user.getUsername());
            userPrincipal.setPassword(user.getPassword());
        }
        return userPrincipal;
    }
}
