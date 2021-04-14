package com.example.leave.services;

import com.example.leave.api.forms.LeavePolicyCreateForm;
import com.example.leave.api.forms.LeavePolicyUpdateForm;
import com.example.leave.models.LeavePolicy;

import java.util.List;

public interface LeavePolicyService {
    List<LeavePolicy> getListLeavePolicy();

    LeavePolicy create(LeavePolicyCreateForm LeavePolicyCreateForm);

    LeavePolicy update(LeavePolicyUpdateForm leavePolicyUpdateForm);

    LeavePolicy getLeavePolicyById(Integer id);

    Integer delete(LeavePolicy leavePolicy);

}