package com.example.leave.services;

import com.example.leave.models.LeaveApplication;

import java.util.List;


public interface LeaveApplicationService {

    LeaveApplication create(LeaveApplication leaveApplication) throws Exception;

    List<LeaveApplication> getByUserId(Integer userId) throws Exception;

}
