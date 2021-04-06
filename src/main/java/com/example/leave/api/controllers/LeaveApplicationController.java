package com.example.leave.api.controllers;

import com.example.leave.api.forms.LeaveApplicationCreateForm;
import com.example.leave.api.forms.LeaveApplicationUpdateForm;
import com.example.leave.api.view.ResponseObject;
import com.example.leave.models.LeaveApplication;
import com.example.leave.models.LeavePolicy;
import com.example.leave.services.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/update")
    public ResponseObject updateApplication(@RequestBody @Valid LeaveApplicationUpdateForm leaveApplicationUpdateForm){
        return new ResponseObject(leaveApplicationService.update(leaveApplicationUpdateForm));
    }

    @GetMapping("/history")
    public ResponseObject getApplicationHistory(){
        List<LeaveApplication> listLeaveApplication = leaveApplicationService.getLeaveApplicationHistory();
        return new ResponseObject(listLeaveApplication);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody LeaveApplication leaveApplication){
        return new ResponseEntity<Integer>(leaveApplicationService.delete(leaveApplication), HttpStatus.OK);
    }
}
