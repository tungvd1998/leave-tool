package com.example.leave.services.Impl;

import com.example.leave.models.LeavePolicy;
import com.example.leave.repositories.LeavePolicyRepository;
import com.example.leave.services.LeavePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LeavePolicyServiceImpl implements LeavePolicyService {

    @Autowired
    private LeavePolicyRepository leavePolicyRepository;

    @Override
    public List<LeavePolicy> getListLeavePolicy(){
        return leavePolicyRepository.findAll();
    }

    @Override
    public LeavePolicy create(LeavePolicy leavePolicy) throws Exception{
        leavePolicy.setCreated(new Date());
        try {
            return leavePolicyRepository.save(leavePolicy);
        } catch (Exception e) {
            throw new Exception("Fail", e);
        }
    }

    public LeavePolicy update(LeavePolicy leavePolicy) throws Exception{
        LeavePolicy leavePolicyDb = getLeavePolicyById(leavePolicy.getId());
        leavePolicyDb.setName(leavePolicy.getName());
        leavePolicyDb.setDuration(leavePolicy.getDuration());
        leavePolicyDb.setContent(leavePolicy.getContent());
        leavePolicyDb.setFromDate(leavePolicy.getFromDate());
        leavePolicyDb.setToDate(leavePolicy.getToDate());
        leavePolicyDb.setCreatorId(leavePolicy.getCreatorId());
        try {
            return leavePolicyRepository.save(leavePolicyDb);
        } catch (Exception e) {
            throw new Exception("Fail", e);
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
