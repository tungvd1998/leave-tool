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
import com.example.leave.services.MailService;
import com.example.leave.utils.DateDiff;
import com.example.leave.utils.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeavePolicyRepository leavePolicyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Override
    @Transactional
    public LeaveApplication createLeaveApplication(LeaveApplicationCreateForm leaveApplicationCreateForm) {
        Optional<LeavePolicy> leavePolicyDb = leavePolicyRepository.findById(leaveApplicationCreateForm.getPolicyId());
        if (!leavePolicyDb.isPresent()) {
            throw new DataNotFoundException(ExceptionConstants.LEAVE_TYPE_NAME_NOT_VALID);
        }
        else {
            LeaveApplication leaveApplication = new LeaveApplication();
            String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
            User user = userRepository.findByUsername(employeeName);
            leaveApplication.setUser(user);
            leaveApplication.setStatus(leaveApplicationCreateForm.getStatus());
            if (DateDiff.getDateDiff(leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate(), TimeUnit.MINUTES) > leavePolicyDb.get().getDuration()){
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_INVALID);
            }
            leaveApplication.setFromDate(leaveApplicationCreateForm.getFromDate());
            leaveApplication.setToDate(leaveApplicationCreateForm.getToDate());
            leaveApplication.setReason(leaveApplicationCreateForm.getReason());
            leaveApplication.setCreated(new Date());
            leaveApplication.setLeavePolicy(leavePolicyDb.get());
            mailService.sendEmail(user.getUsername(),leaveApplication);
            return leaveApplicationRepository.save(leaveApplication);
        }
    }

    @Override
    public List<LeaveApplication> getLeaveApplicationHistory(){
        String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
        return leaveApplicationRepository.getByUsername(employeeName);
    }
    @Override
    public List<LeaveApplication> listAllLeaveApplication() {
        return leaveApplicationRepository.findAll();
    }
}
