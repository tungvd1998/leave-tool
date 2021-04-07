package com.example.leave.infrastructure.security;

import com.example.leave.repositories.AuthorizerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.*;

@Slf4j
@Service("appAuthorizer")
public class PreAuthorizer{

    @Autowired
    private AuthorizerRepository authorizerRepository;
    public boolean authorize(Authentication authentication, String action, String api) {
        boolean isAllow = false;
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
            if (usernamePasswordAuthenticationToken == null) {
                return isAllow;
            }
            String username;
            Object principal = usernamePasswordAuthenticationToken.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
            //Truy vấn vào CSDL theo username
            ArrayList<String> apiPermissions = authorizerRepository.getApiByUsername(username);
            //Nếu có quyền thì
            isAllow = apiPermissions.contains(api);
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw e;
        }
        System.out.println(isAllow);
        return isAllow;
    }
}