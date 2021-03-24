package com.example.leave.services.Impl;

import com.example.leave.api.forms.LeaveApplicationCreateForm;
import com.example.leave.infrastructure.exception.DataNotFoundException;
import com.example.leave.infrastructure.security.ExtractUserAuthentication;
import com.example.leave.models.LeaveApplication;
import com.example.leave.models.LeavePolicy;
import com.example.leave.models.User;
import com.example.leave.repositories.LeaveApplicationRepository;
import com.example.leave.repositories.LeavePolicyRepository;
import com.example.leave.repositories.UserRepository;
import com.example.leave.services.LeaveApplicationService;
import com.example.leave.utils.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeavePolicyRepository leavePolicyRepository;

    @Autowired
    private UserRepository userRepository;

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

    private MyUserDetails myUserDetails;

    @Override
    public LeaveApplication createLeaveApplication(LeaveApplicationCreateForm leaveApplicationCreateForm) {
        Optional<LeavePolicy> leavePolicyDb = leavePolicyRepository.findById(leaveApplicationCreateForm.getPolicyId());
        if (!leavePolicyDb.isPresent()) {
            throw new DataNotFoundException(ExceptionConstants.LEAVE_TYPE_NAME_NOT_VALID);
        }
        LeaveApplication leaveApplication = new LeaveApplication();
        String employeeId = ExtractUserAuthentication.getCurrentUser().getUsername();
//        String employeeUsername = myUserDetails.getUsername();
//        Integer employeeId = userRepository.findIdByUsername(employeeUsername);

        User user = userRepository.findByUsername(employeeId);
        leaveApplication.setUser(user);
        leaveApplication.setStatus(leaveApplicationCreateForm.getStatus());
        leaveApplication.setFromDate(leaveApplicationCreateForm.getFromDate());
        leaveApplication.setReason(leaveApplicationCreateForm.getReason());
        leaveApplication.setToDate(leaveApplicationCreateForm.getToDate());

        return leaveApplicationRepository.save(leaveApplication);
    }

}
