package com.example.leave.api.controllers;

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

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseEntity<?> createPolicy(@RequestBody LeavePolicy leavePolicy) throws Exception {
        return new ResponseEntity<LeavePolicy>(leavePolicyService.create(leavePolicy), HttpStatus.OK);
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    public ResponseEntity<?> updatePolicy(@RequestBody LeavePolicy leavePolicy) throws Exception {
        return new ResponseEntity<LeavePolicy>(leavePolicyService.update(leavePolicy), HttpStatus.OK);
    }

    @RequestMapping(value = {"/delete"}, method = RequestMethod.POST)
    public ResponseEntity<?> delete(@RequestBody LeavePolicy leavePolicy) {
        return new ResponseEntity<Integer>(leavePolicyService.delete(leavePolicy), HttpStatus.OK);
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
}
