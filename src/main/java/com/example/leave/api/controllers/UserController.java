package com.example.leave.api.controllers;

import com.example.leave.infrastructure.security.JwtUtil;
import com.example.leave.models.JwtRequest;
import com.example.leave.models.JwtResponse;
import com.example.leave.models.ResponseObject;
import com.example.leave.models.User;
import com.example.leave.services.AuthenticationService;
import com.example.leave.services.Impl.UserServiceImpl;
import com.example.leave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("sys/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

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
    @PostMapping("/user/logout")
    public ResponseObject logout(RequestBody requestBody){
        return new ResponseObject(userService.logout(null));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getContent(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<User> get(@PathVariable Integer id){
        try{
            User user = userService.get(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }catch(NoSuchElementException e)
        {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/user/update/{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Integer id)
    {
        try {
            User existUser = userService.get(id);
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/user/delete/{id}")
    public ResponseEntity<?> delete(@RequestBody User user, @PathVariable Integer id)
    {
        try {
            User existUser = userService.get(id);
            userService.delete(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
