package com.example.leave.services.Impl;

import com.example.leave.api.forms.LeaveApplicationCreateForm;
import com.example.leave.api.forms.LeaveApplicationUpdateForm;
import com.example.leave.api.view.LeaveApplicationView;
import com.example.leave.infrastructure.constant.ErrorCode;
import com.example.leave.infrastructure.exception.DataNotFoundException;
import com.example.leave.infrastructure.security.ExtractUserAuthentication;
import com.example.leave.models.LeaveApplication;
import com.example.leave.models.LeavePolicy;
import com.example.leave.models.User;
import com.example.leave.repositories.LeaveApplicationRepository;
import com.example.leave.repositories.LeavePolicyRepository;
import com.example.leave.repositories.UserRepository;
import com.example.leave.services.LeaveApplicationService;
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
            List<LeaveApplication> leaveApplicationDb = leaveApplicationRepository.getByUsername(employeeName);

            leaveApplication.setUser(user);
            leaveApplication.setStatus(leaveApplicationCreateForm.getStatus());
//            if (DateDiff.getDateDiff(leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate(), TimeUnit.MINUTES) > leavePolicyDb.get().getDuration()){
//                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_INVALID);
//            }
            long leaveTime = 0;
            leaveTime += DateDiff.getDateDiff(leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate(), TimeUnit.MINUTES);
            if(leaveTime > leavePolicyDb.get().getDuration()) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_INVALID);
            }
            leaveApplication.setLeaveDuration(leaveTime);
            leaveApplication.setFromDate(leaveApplicationCreateForm.getFromDate());
            leaveApplication.setToDate(leaveApplicationCreateForm.getToDate());
            leaveApplication.setReason(leaveApplicationCreateForm.getReason());
            leaveApplication.setCreated(new Date());
            leaveApplication.setLeavePolicy(leavePolicyDb.get());

            return leaveApplicationRepository.save(leaveApplication);
        }
    }

//    public Integer calculateLeaveDuration(String username){
//        return leaveApplicationRepository.calculateLeaveDurationByUsername(username);
//    }

    @Override
    public List<LeaveApplication> getLeaveApplicationHistory(){
        String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
        return leaveApplicationRepository.getByUsername(employeeName);
    }

    @Override
    public LeaveApplication getById(Integer id){
        Optional<LeaveApplication> leaveApplicationDb = leaveApplicationRepository.findById(id);
        if(!leaveApplicationDb.isPresent()){
            throw new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_RECORD_NOT_FOUND);
        }
        return leaveApplicationDb.get();
    }

//    @Override
//    public LeaveApplication update(LeaveApplicationUpdateForm leaveApplicationUpdateForm){
//        LeaveApplication leaveApplicationDb = getById(leaveApplicationUpdateForm.getId());
//        if (leaveApplicationDb.getStatus().equals("OK")){
//            Optional<LeavePolicy> leavePolicyDb = leavePolicyRepository.findById(leaveApplicationUpdateForm.getPolicyId());
//            if(DateDiff.getDateDiff(leaveApplicationUpdateForm.getFromDate(), leaveApplicationUpdateForm.getToDate(), TimeUnit.MINUTES) > leaveApplicationUpdateForm.getPolicyId()) {
//                leaveApplicationDb.setFromDate(leaveApplicationUpdateForm.getFromDate());
//                leaveApplicationDb.setToDate(leaveApplicationUpdateForm.getToDate());
//                leaveApplicationDb.setReason(leaveApplicationUpdateForm.getReason());
//            }
//        }
//        throw new DataNotFoundException()
//    }
}
