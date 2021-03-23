package com.example.leave.services.Impl;

import com.example.leave.infrastructure.exception.DataNotFoundException;
import com.example.leave.infrastructure.security.ExtractUserAuthentication;
import com.example.leave.models.LeaveApplication;
import com.example.leave.models.User;
import com.example.leave.repositories.LeaveApplicationRepository;
import com.example.leave.services.LeaveApplicationService;
import com.example.leave.utils.ExceptionConstants;
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
    public LeaveApplication create(LeaveApplication leaveApplication) throws Exception{
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
        if(listLeaveApplication == null){
            throw new  Exception("User not found with userId: " + userId);
        }
        return listLeaveApplication;
    }

    @Override
    public LeaveApplication createLeaveApplication(LeaveApplication leaveApplication){
        if (leaveApplication.getLeavePolicy() == null){
            throw new DataNotFoundException(ExceptionConstants.LEAVE_TYPE_NAME_NOT_VALID);
        }
        Integer employeeId = ExtractUserAuthentication.getCurrentUser().getId();
        User user = new User();
        user.set(employeeId);

        leaveApplication.setUser(employeeId);
        leaveApplication.setStatus(leaveApplication.getStatus());

        LeaveApplication employeeLeaveApplication = leaveApplicationRepository.save(leaveApplication);
        return employeeLeaveApplication;
    }

}
