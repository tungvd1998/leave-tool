package com.example.leave.services.Impl;

import com.example.leave.api.forms.LeavePolicyCreateForm;
import com.example.leave.api.forms.LeavePolicyUpdateForm;
import com.example.leave.infrastructure.exception.DataNotFoundException;
import com.example.leave.infrastructure.security.ExtractUserAuthentication;
import com.example.leave.models.LeavePolicy;
import com.example.leave.models.User;
import com.example.leave.repositories.LeavePolicyRepository;
import com.example.leave.repositories.UserRepository;
import com.example.leave.services.LeavePolicyService;
import com.example.leave.utils.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LeavePolicyServiceImpl implements LeavePolicyService {

    @Autowired
    private LeavePolicyRepository leavePolicyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<LeavePolicy> getListLeavePolicy(){
        return leavePolicyRepository.findAll();
    }

    @Override
    public LeavePolicy create(LeavePolicyCreateForm leavePolicyCreateForm){
        String employeeUsername = ExtractUserAuthentication.getCurrentUser().getUsername();
        User user = userRepository.findByUsername(employeeUsername);
        if ((leavePolicyCreateForm.getFromDate()).compareTo(leavePolicyCreateForm.getToDate()) > 0
                || (leavePolicyCreateForm.getFromDate()).compareTo(new Date()) < 0){
            throw new DataNotFoundException(ExceptionConstants.EFFECTIVE_TIME_INVALID);
        }
        LeavePolicy leavePolicy = new LeavePolicy();
        leavePolicy.setCreated(new Date());
        leavePolicy.setCreatorId(user.getId());
        leavePolicy.setName(leavePolicyCreateForm.getName());
        leavePolicy.setDuration(leavePolicyCreateForm.getDuration());
        leavePolicy.setContent(leavePolicyCreateForm.getContent());
        leavePolicy.setFromDate(leavePolicyCreateForm.getFromDate());
        leavePolicy.setToDate(leavePolicyCreateForm.getToDate());
        try {
            return leavePolicyRepository.save(leavePolicy);
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionConstants.ERRORS);
        }
    }

    public LeavePolicy update(LeavePolicyUpdateForm leavePolicyUpdateForm){
        LeavePolicy leavePolicyDb = getLeavePolicyById(leavePolicyUpdateForm.getId());
        if ((leavePolicyUpdateForm.getFromDate()).compareTo(leavePolicyUpdateForm.getToDate()) > 0
                || (leavePolicyUpdateForm.getFromDate()).compareTo(leavePolicyDb.getCreated()) < 0){
            throw new DataNotFoundException(ExceptionConstants.EFFECTIVE_TIME_INVALID);
        }
        leavePolicyDb.setName(leavePolicyUpdateForm.getName());
        leavePolicyDb.setDuration(leavePolicyUpdateForm.getDuration());
        leavePolicyDb.setContent(leavePolicyUpdateForm.getContent());
        leavePolicyDb.setFromDate(leavePolicyUpdateForm.getFromDate());
        leavePolicyDb.setToDate(leavePolicyUpdateForm.getToDate());
        Date dateModify = new Date();
        leavePolicyDb.setModified(dateModify);
        try {
            return leavePolicyRepository.save(leavePolicyDb);
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionConstants.ERRORS);
        }
    }

    @Override
    public LeavePolicy getLeavePolicyById(Integer id){
        return leavePolicyRepository.findById(id).get();
    }

    @Override
    public Integer delete(LeavePolicy leavePolicy){
        LeavePolicy leavePolicyDb = getLeavePolicyById(leavePolicy.getId());
        leavePolicyRepository.delete(leavePolicyDb);
        return 0;
    }
}
