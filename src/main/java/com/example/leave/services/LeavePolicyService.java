package com.example.leave.services;

import com.example.leave.models.LeavePolicy;

import java.util.List;

public interface LeavePolicyService {
    List<LeavePolicy> getListLeavePolicy();

    LeavePolicy create(LeavePolicy leavePolicy) throws Exception;

    LeavePolicy update(LeavePolicy leavePolicy) throws Exception;

    LeavePolicy getLeavePolicyById(Integer id);

    Integer delete(LeavePolicy leavePolicy);

}
