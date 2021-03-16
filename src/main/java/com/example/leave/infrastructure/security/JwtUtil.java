package com.example.leave.infrastructure.security;

import com.example.leave.models.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    private final String JWT_SECRET = "lodaaaaaa";

    //Thời gian có hiệu lực của chuỗi jwt
    private final long JWT_EXPIRATION = 604800000L;

    public String generateToken(User user){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        // do thuat toan gen token no khong tuong thich vs ham signWith
        // hinh nhu thieu casi gi do
        // cai nay n gen ra token nhung lieu decode thi se ra nhung thong tin gi
        //ok roi day
//        trong cai inplement userdetailservice no tra ve username ok hieu r

        return Jwts.builder().setSubject(user.getUsername().toString()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
//        return Jwts.builder().setSubject(user.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(expiryDate).signWith(SignatureAlgorithm.ES512, JWT_SECRET).compact();
    }

    public String getUserNameFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        }catch (MalformedJwtException ex){
            log.error("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            log.error("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            log.error("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            log.error("JWT claims string is empty.");
        }
        return false;
    }


}
