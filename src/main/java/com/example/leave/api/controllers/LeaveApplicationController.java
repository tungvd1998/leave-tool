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
@RequestMapping("sys/v1/leaveApplications")
public class LeaveApplicationController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @PostMapping("/create")
    public ResponseEntity<?> createApplication(@RequestBody LeaveApplicationCreateForm leaveApplicationCreateForm){
        return new ResponseEntity<>(leaveApplicationService.createLeaveApplication(leaveApplicationCreateForm), HttpStatus.OK);
    }

    @GetMapping("/history")
    public List<LeaveApplication> getApplicationHistory(){
        List<LeaveApplication> listLeaveApplication = leaveApplicationService.getLeaveApplicationHistory();
        return listLeaveApplication;
    }

}
