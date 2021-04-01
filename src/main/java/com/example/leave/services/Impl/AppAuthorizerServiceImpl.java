package com.example.leave.services.Impl;

import com.example.leave.repositories.AppAuthorizerRepository;
import com.example.leave.services.AppAuthorizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.util.*;

@Service("appAuthorizer")
public class AppAuthorizerServiceImpl implements AppAuthorizerService {

    @Autowired
    private AppAuthorizerRepository appAuthorizerRepository;

    private final Logger logger = LoggerFactory.getLogger(AppAuthorizerServiceImpl.class);

    @Override
    public boolean authorize(Authentication authentication, String action, Object callerObj) {
        String securedPath = extractSecuredPath(callerObj);
        System.out.println("chuoi kiem tra:   " + securedPath);
        System.out.println("authentication :" + authentication);
        if (securedPath==null || "".equals(securedPath.trim())) {//login, logout
            return true;
        }
        String menuCode = securedPath.substring(1);//Bỏ dấu "/" ở đầu Path
        System.out.println(menuCode);
        boolean isAllow = false;
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
            if (usernamePasswordAuthenticationToken==null){
                return isAllow;
            }

            String username;
            Object principal = usernamePasswordAuthenticationToken.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            System.out.println("username"+ username);
            String role = appAuthorizerRepository.findNameRole(username);
            //Truy vấn vào CSDL theo username
            //Nếu có quyền thì
            System.out.println(role);
            System.out.println("menuCOde  :" + menuCode);
            if(menuCode.equals(role))
            {
                isAllow = true;
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            throw e;
        }
        System.out.println(isAllow);
        return isAllow;
    }

    // Lay ra securedPath duoc Annotate RequestMapping trong Controller
    private String extractSecuredPath(Object callerObj) {
        Class<?> clazz = ResolvableType.forClass(callerObj.getClass()).getRawClass();
        System.out.println("callerObj    :" + callerObj+"?");
        System.out.println("clazz   :" + clazz);
        Optional<Annotation> annotation = Arrays.asList(clazz.getAnnotations()).stream().filter((ann) -> {
            System.out.println(ann instanceof RequestMapping);
            return ann instanceof RequestMapping;
        }).findFirst();

        logger.debug("FOUND CALLER CLASS: {}", ResolvableType.forClass(callerObj.getClass()).getType().getTypeName());
        System.out.println("annotation   :" +  annotation);
        if (annotation.isPresent()) {

            String m = ((RequestMapping) annotation.get()).value()[0];
            System.out.println("resuft" + m);
            return m;

        }
        return null;
    }
}

