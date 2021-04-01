package com.example.leave.api.controllers;

import com.example.leave.api.forms.LeaveApplicationCreateForm;
import com.example.leave.models.LeaveApplication;
import com.example.leave.services.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaveApplication")
public class LeaveApplicationController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseEntity<?> createApplication(@RequestBody LeaveApplicationCreateForm leaveApplicationCreateForm){
        return new ResponseEntity<>(leaveApplicationService.createLeaveApplication(leaveApplicationCreateForm), HttpStatus.OK);
    }

    @RequestMapping(value = {"/history"}, method = RequestMethod.GET)
    public List<LeaveApplication> getApplicationHistory(){
        List<LeaveApplication> listLeaveApplication = leaveApplicationService.getLeaveApplicationHistory();
        return listLeaveApplication;
    }

}
