package com.example.leave.api.controllers;

import com.example.leave.infrastructure.security.JwtUtil;
import com.example.leave.models.User;
import com.example.leave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("sys/v1/register")
    public User register(@RequestBody User user){
        return userService.createUser(user);
    }


    @PostMapping("/sys/v1/login")
    public String login(@RequestBody User user){
        return  userService.login(user);
    }
}
