package com.example.leave.api.controllers;

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

    @PostMapping("/createApplication")
    public ResponseEntity<?> createApplication(@RequestBody LeaveApplication leaveApplication) throws Exception {
        return new ResponseEntity<LeaveApplication>(leaveApplicationService.create(leaveApplication), HttpStatus.OK);
    }

    @GetMapping("/getApplicationByUserId/{userId}")
    public List<LeaveApplication> getApplicationByUserId(@PathVariable Integer userId) throws Exception {
        List<LeaveApplication> listLeaveApplication = leaveApplicationService.getByUserId(userId);
        return listLeaveApplication;
    }


}
