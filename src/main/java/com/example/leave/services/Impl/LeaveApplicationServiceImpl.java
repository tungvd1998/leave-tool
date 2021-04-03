package com.example.leave.services.Impl;

import com.example.leave.api.forms.LeaveApplicationCreateForm;
import com.example.leave.api.forms.LeaveApplicationUpdateForm;
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
            String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
            User user = userRepository.findByUsername(employeeName);
            Integer leaveDuration = calculateLeaveDuration(employeeName, leaveApplicationCreateForm.getFromDate().getMonth() + 1);
            if(leaveDuration >= leavePolicyDb.get().getDuration()){
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_TIME_OUT);
            }
            long leaveTime = 0;
            leaveTime += DateDiff.getDateDiff(leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate(), TimeUnit.MINUTES);
            if(leaveTime > leavePolicyDb.get().getDuration() || (leaveTime + leaveDuration) > leavePolicyDb.get().getDuration()) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_INVALID);
            }
            Date dateCreate = new Date();
            if ((leaveApplicationCreateForm.getFromDate()).compareTo(leaveApplicationCreateForm.getToDate()) > 0
                    || (leaveApplicationCreateForm.getFromDate()).compareTo(dateCreate) < 0){
                throw new DataNotFoundException(ExceptionConstants.DATE_LEAVE_ILLEGAL);
            }
            if (checkDateLeaveExist(employeeName, leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate())){
                throw new DataNotFoundException(ExceptionConstants.DATE_LEAVE_EXIST);
            }
            LeaveApplication leaveApplication = new LeaveApplication();
            leaveApplication.setUser(user);
            leaveApplication.setStatus(leaveApplicationCreateForm.getStatus());
            leaveApplication.setLeaveDuration(leaveTime);
            leaveApplication.setFromDate(leaveApplicationCreateForm.getFromDate());
            leaveApplication.setToDate(leaveApplicationCreateForm.getToDate());
            leaveApplication.setReason(leaveApplicationCreateForm.getReason());
            leaveApplication.setCreated(dateCreate);
            leaveApplication.setLeavePolicy(leavePolicyDb.get());

            return leaveApplicationRepository.save(leaveApplication);
        }
    }

    public Integer calculateLeaveDuration(String username, Integer month){
        Integer leaveDuration = leaveApplicationRepository.calculateLeaveDurationByUsername(username, month);
        if (leaveDuration == null){
            return 0;
        }else {
            return leaveDuration;
        }
    }

    public Boolean checkDateLeaveExist(String username, Date fromDate, Date toDate){
        Integer check = leaveApplicationRepository.getFromDate(username, fromDate, toDate);
        if (check == 1){
            return true;
        }
        return false;
    }
    
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

    @Override
    public LeaveApplication update(LeaveApplicationUpdateForm leaveApplicationUpdateForm){
        LeaveApplication leaveApplication = getById(leaveApplicationUpdateForm.getId());
        if (leaveApplication.getStatus().equals("OK")){
            throw new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_ACTION_ALREADY_TAKEN);
        }else {
            String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
            Optional<LeavePolicy> leavePolicyDb = leavePolicyRepository.findById(leaveApplicationUpdateForm.getPolicyId());
            if (!leavePolicyDb.isPresent()) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_TYPE_NAME_NOT_VALID);
            }
            Date dateModify = new Date();
            if ((leaveApplicationUpdateForm.getFromDate()).compareTo(leaveApplicationUpdateForm.getToDate()) > 0
                    || (leaveApplicationUpdateForm.getFromDate()).compareTo(dateModify) < 0){
                throw new DataNotFoundException(ExceptionConstants.DATE_LEAVE_ILLEGAL);
            }
            Integer leaveDuration = calculateLeaveDuration(employeeName, leaveApplicationUpdateForm.getFromDate().getMonth() + 1);
            if(leaveDuration >= leavePolicyDb.get().getDuration() ){
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_TIME_OUT);
            }
            Long leaveTime = DateDiff.getDateDiff(leaveApplicationUpdateForm.getFromDate(), leaveApplicationUpdateForm.getToDate(), TimeUnit.MINUTES);
            if(leaveTime > leavePolicyDb.get().getDuration() || (leaveTime + (leaveDuration - leaveApplication.getLeaveDuration()) > leavePolicyDb.get().getDuration())) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_INVALID);
            }
            if (checkDateLeaveExist(employeeName, leaveApplicationUpdateForm.getFromDate(), leaveApplicationUpdateForm.getToDate())){
                throw new DataNotFoundException(ExceptionConstants.DATE_LEAVE_EXIST);
            }
            leaveApplication.setFromDate(leaveApplicationUpdateForm.getFromDate());
            leaveApplication.setToDate(leaveApplicationUpdateForm.getToDate());
            leaveApplication.setReason(leaveApplicationUpdateForm.getReason());
            leaveApplication.setModified(dateModify);
            leaveApplication.setLeaveDuration(leaveTime);
            leaveApplication.setLeavePolicy(leavePolicyDb.get());
            try {
                return leaveApplicationRepository.save(leaveApplication);
            }catch (Exception e){
                throw new DataNotFoundException(ExceptionConstants.ERRORS);
            }
        }
    }
}
