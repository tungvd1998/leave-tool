package com.example.leave.services;

import com.example.leave.api.forms.LeaveApplicationCreateForm;
import com.example.leave.api.forms.LeaveApplicationUpdateForm;
import com.example.leave.models.LeaveApplication;
import com.example.leave.models.User;

import java.util.List;


public interface LeaveApplicationService {

    LeaveApplication createLeaveApplication(LeaveApplicationCreateForm leaveApplicationCreateForm);

    List<LeaveApplication> getLeaveApplicationHistory();

    LeaveApplication getById(Integer id);

    LeaveApplication update(LeaveApplicationUpdateForm leaveApplicationUpdateForm);

    Integer delete(LeaveApplication leaveApplication);

}
