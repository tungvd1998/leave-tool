package com.example.leave.api.controllers;

import com.example.leave.infrastructure.security.JwtUtil;
import com.example.leave.models.LeavePolicy;
import com.example.leave.models.User;
import com.example.leave.services.AuthenticationService;
import com.example.leave.services.Impl.UserServiceImpl;
import com.example.leave.services.LeavePolicyService;
import com.example.leave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private LeavePolicyService leavePolicyService;

    @RequestMapping(value = {"/user/getAll"}, method = RequestMethod.GET)
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'REGISTER', this)")
    public List<User> list() {
        return userService.listAllUser();
    }


    @RequestMapping(value = {"/leavePolicy/create"}, method = RequestMethod.POST)
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'CREATE', this)")
    public ResponseEntity<?> createPolicy(@RequestBody LeavePolicy leavePolicy) throws Exception {
        return new ResponseEntity<LeavePolicy>(leavePolicyService.create(leavePolicy), HttpStatus.OK);
    }

    @RequestMapping(value = {"/leavePolicy/update"}, method = RequestMethod.POST)
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'UPDATE', this)")
    public ResponseEntity<?> updatePolicy(@RequestBody LeavePolicy leavePolicy) throws Exception {
        return new ResponseEntity<LeavePolicy>(leavePolicyService.update(leavePolicy), HttpStatus.OK);
    }

    @RequestMapping(value = {"/leavePolicy/delete"}, method = RequestMethod.POST)
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'DELETE', this)")
    public ResponseEntity<?> delete(@RequestBody LeavePolicy leavePolicy) {
        return new ResponseEntity<Integer>(leavePolicyService.delete(leavePolicy), HttpStatus.OK);
    }

}
