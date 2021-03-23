package com.example.leave.api.controllers;

import com.example.leave.models.LeavePolicy;
import com.example.leave.services.LeavePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("sys/v1/leavePolicies")
public class LeavePolicyController {

    @Autowired
    private LeavePolicyService leavePolicyService;

    @GetMapping("/getAllPolicies")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEMBER')")
    public List<LeavePolicy> getAllPolicy(){
        List<LeavePolicy> listPolicy = leavePolicyService.getListLeavePolicy();
        return listPolicy;
    }

    @PostMapping("/createPolicy")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createPolicy(@RequestBody LeavePolicy leavePolicy) throws Exception{
        return new ResponseEntity<LeavePolicy>(leavePolicyService.create(leavePolicy), HttpStatus.OK);
    }

    @GetMapping("/getPolicyById/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEMBER')")
    public ResponseEntity<LeavePolicy> getPolicyById(@PathVariable Integer id){
        try{
            LeavePolicy leavePolicy = leavePolicyService.getLeavePolicyById(id);
            return new ResponseEntity<LeavePolicy>(leavePolicy, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<LeavePolicy>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/updatePolicy")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updatePolicy(@RequestBody LeavePolicy leavePolicy) throws Exception{
        return new ResponseEntity<LeavePolicy>(leavePolicyService.update(leavePolicy), HttpStatus.OK);
    }

    @PostMapping("/deletePolicy")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@RequestBody LeavePolicy leavePolicy){
        return new ResponseEntity<Integer>(leavePolicyService.delete(leavePolicy), HttpStatus.OK);
    }
}
