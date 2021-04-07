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
import com.example.leave.utils.DayOfWeek;
import com.example.leave.utils.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
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

    @Value("${startWorkTime}")
    private Integer startWorkTime;

    @Value("${stopMorningWorkTime}")
    private Integer stopMorningWorkTime;

    @Value("${startAfternoonWorkTime}")
    private Integer startAfternoonWorkTime;

    @Value("${endWorkTime}")
    private Integer endWorkTime;

    @Override
    @Transactional
    public LeaveApplication createLeaveApplication(LeaveApplicationCreateForm leaveApplicationCreateForm) {
        Optional<LeavePolicy> leavePolicyDb = leavePolicyRepository.findById(leaveApplicationCreateForm.getPolicyId());
        if (!leavePolicyDb.isPresent()) {
            throw new DataNotFoundException(ExceptionConstants.LEAVE_TYPE_NAME_INVALID);
        }
        else {
            String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
            User user = userRepository.findByUsername(employeeName);
            Integer leaveDuration = calculateLeaveDurationFormDb(employeeName, leaveApplicationCreateForm.getFromDate().getMonth() + 1);
            Integer dayOfWeek = DayOfWeek.getDayNumberOld(leaveApplicationCreateForm.getFromDate());
            System.out.println(dayOfWeek);
            if(leaveDuration >= leavePolicyDb.get().getDuration()){
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_TIME_OUT);
            }
            long leaveTime = calculateLeaveDuration(leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate());
            if(leaveTime > leavePolicyDb.get().getDuration() || (leaveTime + leaveDuration) > leavePolicyDb.get().getDuration()) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_INVALID);
            }
            if((leaveApplicationCreateForm.getFromDate().getHours() < startWorkTime || leaveApplicationCreateForm.getFromDate().getHours() > endWorkTime) || (leaveApplicationCreateForm.getToDate().getHours() < startWorkTime || leaveApplicationCreateForm.getToDate().getHours() > endWorkTime)){
                throw new DataNotFoundException(ExceptionConstants.NOT_WORKING_TIME);
            }
            if (DayOfWeek.getDayNumberOld(leaveApplicationCreateForm.getFromDate()) == 7 || DayOfWeek.getDayNumberOld(leaveApplicationCreateForm.getToDate()) == 7){
                throw new DataNotFoundException(ExceptionConstants.DATE_LEAVE_ILLEGAL);
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

    public Integer calculateLeaveDurationFormDb(String username, Integer month){
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

    public Long calculateLeaveDuration(Date fromDate, Date toDate){
        long leaveDuration = DateDiff.getDateDiff(fromDate, toDate, TimeUnit.MINUTES);
        if (fromDate.getDate() == toDate.getDate()){
            if (fromDate.getHours() >= startWorkTime && toDate.getHours() >= startAfternoonWorkTime){
                return leaveDuration - 60;
            }else if (fromDate.getHours() >= startWorkTime && toDate.getHours() == stopMorningWorkTime){
                return leaveDuration - toDate.getMinutes();
            }else if (fromDate.getHours() == stopMorningWorkTime && toDate.getHours() <= endWorkTime){
                return leaveDuration - fromDate.getMinutes();
            }else if (fromDate.getHours() == stopMorningWorkTime && toDate.getHours() == stopMorningWorkTime){
                return leaveDuration - (toDate.getMinutes() - toDate.getMinutes());
            }
            return leaveDuration;
        } else {
            Integer day = toDate.getDate() - fromDate.getDate();
            for (int value = 1; value <= day; value++){
                if (value == day){
                    leaveDuration = leaveDuration - (16 * 60 * value) - 60;
                }
            }
            return leaveDuration;
        }
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
                throw new DataNotFoundException(ExceptionConstants.LEAVE_TYPE_NAME_INVALID);
            }
            Date dateModify = new Date();
            if ((leaveApplicationUpdateForm.getFromDate()).compareTo(leaveApplicationUpdateForm.getToDate()) > 0
                    || (leaveApplicationUpdateForm.getFromDate()).compareTo(dateModify) < 0){
                throw new DataNotFoundException(ExceptionConstants.DATE_LEAVE_ILLEGAL);
            }
            if((leaveApplicationUpdateForm.getFromDate().getHours() < startWorkTime || leaveApplicationUpdateForm.getFromDate().getHours() > endWorkTime) || (leaveApplicationUpdateForm.getToDate().getHours() < startWorkTime || leaveApplicationUpdateForm.getToDate().getHours() > endWorkTime)){
                throw new DataNotFoundException(ExceptionConstants.NOT_WORKING_TIME);
            }
            Integer leaveDuration = calculateLeaveDurationFormDb(employeeName, leaveApplicationUpdateForm.getFromDate().getMonth() + 1);
            if(leaveDuration >= leavePolicyDb.get().getDuration() ){
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_TIME_OUT);
            }
            Long leaveTime = calculateLeaveDuration(leaveApplicationUpdateForm.getFromDate(), leaveApplicationUpdateForm.getToDate());
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

    @Override
    public Integer delete(LeaveApplication leaveApplication){
        LeaveApplication leaveApplicationDb = getById(leaveApplication.getId());
        if (leaveApplicationDb.getStatus().equals("OK")){
            throw new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_ACTION_ALREADY_TAKEN);
        }
        leaveApplicationRepository.delete(leaveApplicationDb);
        return 0;
    }
}
