package com.example.leave.services.Impl;

import com.example.leave.infrastructure.security.JwtUtil;
import com.example.leave.models.JwtResponse;
import com.example.leave.models.User;
import com.example.leave.repositories.UserRepository;
import com.example.leave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Override
    public User createUser(User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setCreated(new Date());
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
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

    @Override
    public String login(UserDetails userDetails){
        final String token = jwtUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public String logout(String request) {
        return null;
    }

    @Override
    public User get(Integer id) {
        return userRepository.getById(id);
    }

    @Override
    public void save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setStatus(0);
        userRepository.save(user);
    }
}
