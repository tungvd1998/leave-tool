package com.example.leave.services;

import com.example.leave.models.User;
import com.example.leave.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User findByUsername(String username);
    String loginUser(UserDetails userDetails, HttpServletResponse httpServletResponse);
    String logoutUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    User getByIdUser(Integer id);
    void saveUser(User user);
    void deleteUser(User user);
    List<User> listAllUser();


}
