package com.example.leave.services.Impl;

import com.example.leave.repositories.TokenRepository;
import com.example.leave.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Token;

public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public Token createToken(Token token){
        return tokenRepository.saveAndFlush(token);
    }
}
