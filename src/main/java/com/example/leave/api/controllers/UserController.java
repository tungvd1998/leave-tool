package com.example.leave.api.controllers;

import com.example.leave.infrastructure.security.JwtUtil;
import com.example.leave.models.JwtRequest;
import com.example.leave.models.JwtResponse;
import com.example.leave.models.LeavePolicy;
import com.example.leave.models.User;
import com.example.leave.services.AuthenticationService;
import com.example.leave.services.Impl.AuthenticationServiceImpl;
import com.example.leave.services.Impl.UserServiceImpl;
import com.example.leave.services.LeavePolicyService;
import com.example.leave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("sys/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImpl jwtUserDetailsService;

    @PostMapping("/user/register")
    public User register(@RequestBody User user){
        return userService.createUser(user);
    }


    @PostMapping(value = "/user/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        return ResponseEntity.ok(new JwtResponse(userService.login(userDetails)));
    }

    @GetMapping("/test/hello")
    public ResponseEntity<String> getContent(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }


}
