package com.example.leave.api.controllers;

import com.example.leave.api.forms.LeaveApplicationCreateForm;
import com.example.leave.api.view.ResponseObject;
import com.example.leave.models.LeaveApplication;
import com.example.leave.services.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("sys/v1/leaveApplications")
public class LeaveApplicationController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @PostMapping("/create")
    public ResponseObject createApplication(@RequestBody @Valid LeaveApplicationCreateForm leaveApplicationCreateForm){
        return new ResponseObject(leaveApplicationService.createLeaveApplication(leaveApplicationCreateForm));
    }

//    @PostMapping("/update")
//    public ResponseObject updateApplication(@RequestBody @Valid LeaveApplication leaveApplication){
//        return new ResponseObject(leaveApplicationService.update(leaveApplication));
//    }

    @GetMapping("/history")
    public ResponseObject getApplicationHistory(){
        List<LeaveApplication> listLeaveApplication = leaveApplicationService.getLeaveApplicationHistory();
        return new ResponseObject(listLeaveApplication);
    }

}
