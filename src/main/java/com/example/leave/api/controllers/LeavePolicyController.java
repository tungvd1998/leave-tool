package com.example.leave.api.controllers;

import com.example.leave.api.forms.LeavePolicyCreateForm;
import com.example.leave.api.forms.LeavePolicyUpdateForm;
import com.example.leave.models.LeavePolicy;
import com.example.leave.services.LeavePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("sys/v1/leavePolicy")
public class LeavePolicyController {

    @Autowired
    private LeavePolicyService leavePolicyService;


    @GetMapping("/getAll")
    public List<LeavePolicy> getAllPolicy(){
        List<LeavePolicy> listPolicy = leavePolicyService.getListLeavePolicy();
        return listPolicy;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPolicy(@RequestBody LeavePolicyCreateForm leavePolicyCreateForm){
        return new ResponseEntity<LeavePolicy>(leavePolicyService.create(leavePolicyCreateForm), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeavePolicy> getPolicyById(@PathVariable Integer id) {
        try {
            LeavePolicy leavePolicy = leavePolicyService.getLeavePolicyById(id);
            return new ResponseEntity<LeavePolicy>(leavePolicy, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<LeavePolicy>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updatePolicy(@RequestBody LeavePolicyUpdateForm leavePolicyUpdateForm) throws Exception{
        return new ResponseEntity<LeavePolicy>(leavePolicyService.update(leavePolicyUpdateForm), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody LeavePolicy leavePolicy) {
        return new ResponseEntity<Integer>(leavePolicyService.delete(leavePolicy), HttpStatus.OK);
    }

}
