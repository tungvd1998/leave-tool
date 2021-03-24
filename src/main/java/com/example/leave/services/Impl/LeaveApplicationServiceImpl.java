package com.example.leave.services.Impl;

import com.example.leave.models.LeaveApplication;
import com.example.leave.repositories.LeaveApplicationRepository;
import com.example.leave.services.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Override
    public LeaveApplication create(LeaveApplication leaveApplication) throws Exception {
        leaveApplication.setCreated(new Date());
        try {
            return leaveApplicationRepository.save(leaveApplication);
        } catch (Exception e) {
            throw new Exception("Fail", e);
        }
    }

    @Override
    public List<LeaveApplication> getByUserId(Integer userId) throws Exception {
        List<LeaveApplication> listLeaveApplication = leaveApplicationRepository.getLeaveApplicationByUserId(userId);
        if (listLeaveApplication == null) {
            throw new Exception("User not found with userId: " + userId);
        }
        return listLeaveApplication;
    }

}
