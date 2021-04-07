package com.example.leave.api.controllers;

import com.example.leave.api.forms.LeaveApplicationCreateForm;
import com.example.leave.models.LeaveApplication;
import com.example.leave.services.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaveApplication")
public class LeaveApplicationController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @RequestMapping(value = {"/getAll"}, method = RequestMethod.GET)
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'getAll', '/leaveApplication/getAll')")
    public List<LeaveApplication> get() {
        return leaveApplicationService.listAllLeaveApplication();
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'create', '/leaveApplication/create')")
    public ResponseEntity<?> createApplication(@RequestBody LeaveApplicationCreateForm leaveApplicationCreateForm){
        return new ResponseEntity<>(leaveApplicationService.createLeaveApplication(leaveApplicationCreateForm), HttpStatus.OK);
    }

    @RequestMapping(value = {"/getHistory"}, method = RequestMethod.GET)
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'getHistory', '/leaveApplication/getHistory')")
    public List<LeaveApplication> getApplicationHistory(){
        List<LeaveApplication> listLeaveApplication = leaveApplicationService.getLeaveApplicationHistory();
        return listLeaveApplication;
    }

}
