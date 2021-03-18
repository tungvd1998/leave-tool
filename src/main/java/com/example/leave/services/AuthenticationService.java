package com.example.leave.services;

public interface AuthenticationService {
    void authenticate(String username, String password) throws Exception;
}
