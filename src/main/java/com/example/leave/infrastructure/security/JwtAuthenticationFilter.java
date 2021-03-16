package com.example.leave.infrastructure.security;

import com.example.leave.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil tokenProvider;
//
//    @Autowired
//    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        try {
//            String urlPath = request.getRequestURI();
//            if (urlPath.startsWith("/sys/v1")){
//                filterChain.doFilter(request,response);
//            }else {
//               response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"UNAUTHORIZED");
//                System.out.println("2");
//               return;
////            }
//            String jwt = getJwtFromRequest(request);
//            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
//                String userName = tokenProvider.getUserNameFromJWT(jwt);
//                User user = (User) userDetailsService.loadUserByUsername(userName);
//                if (user != null){
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null);
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                }
//           }
//        } catch (Exception ex) {
//            log.error("failed on set user authentication", ex);
//        }
        filterChain.doFilter(request, response);
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
