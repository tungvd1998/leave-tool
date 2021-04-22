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
        } else {
            String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
            User user = userRepository.findByUsername(employeeName);
            long leaveTime = leaveDuration(leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate());
            if (leaveTime > leavePolicyDb.get().getDuration()){
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_INVALID);
            }
            if (isLeaveDurationTimeOut(employeeName, leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate(), leaveTime, leavePolicyDb.get().getMaxDurationInMonth())) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_TIME_OUT);
            }
            if (isNotWorkingTime(leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate())
                    || isSaturdaySunday(leaveApplicationCreateForm.getFromDate())
                    || isSaturdaySunday(leaveApplicationCreateForm.getToDate())) {
                throw new DataNotFoundException(ExceptionConstants.NOT_WORKING_TIME);
            }
            Date dateCreate = new Date();
            if ((leaveApplicationCreateForm.getFromDate()).compareTo(leaveApplicationCreateForm.getToDate()) > 0
                    || (leaveApplicationCreateForm.getFromDate()).compareTo(dateCreate) < 0) {
                throw new DataNotFoundException(ExceptionConstants.DATE_LEAVE_ILLEGAL);
            }
            if (isDateLeaveExist(employeeName, leaveApplicationCreateForm.getFromDate(), leaveApplicationCreateForm.getToDate())) {
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

    public Long getLeaveDurationWithSameMonthFormDb(String username, Integer month) {
        Long leaveDuration = leaveApplicationRepository.calculateLeaveDurationByUsername(username, month);
        if (leaveDuration == null) {
            return Long.valueOf(0);
        }
        return leaveDuration;
    }

    public Boolean isDateLeaveExist(String username, Date fromDate, Date toDate) {
        Integer timeLeaveExists = leaveApplicationRepository.getFromDate(username, fromDate, toDate);
        if (timeLeaveExists == 1) {
            return true;
        }
        return false;
    }

    public Boolean isNotWorkingTime(Date fromDate, Date toDate) {
        if (DateDiff.getHour(fromDate) < startWorkTime || DateDiff.getHour(fromDate) >= endWorkTime || DateDiff.getHour(fromDate) == stopMorningWorkTime
                || DateDiff.getHour(toDate) < startWorkTime || DateDiff.getHour(toDate) > endWorkTime || (DateDiff.getHour(toDate) == stopMorningWorkTime && DateDiff.getMinutes(toDate) > 0)) {
            return true;
        }
        return false;
    }

    public Boolean isSaturdaySunday(Date date) {
        Integer day = DayOfWeek.getDayOfWeek(date);
        if (day == 1) {
            return true;
        } else if (day == 7) {
            if ((DateDiff.getHour(date) == 11 && DateDiff.getMinutes(date) > 30)
                    || DateDiff.getHour(date) > 11)
                return true;
            return false;
        }
        return false;
    }

    public Long leaveDuration(Date fromDate, Date toDate) {
        long leaveDuration = DateDiff.getDateDiff(fromDate, toDate, TimeUnit.MINUTES);
        if (DateDiff.getDate(fromDate) == DateDiff.getDate(toDate)) {
            if (DateDiff.getHour(fromDate) <= stopMorningWorkTime && DateDiff.getHour(toDate) >= startAfternoonWorkTime) {
                return leaveDuration - 60;
            }
            return leaveDuration;
        } else {
            Integer saturdaySundayCount = DayOfWeek.saturdaySundayCount(fromDate, toDate);
            Integer workingDays = DayOfWeek.workingDayCount(fromDate, toDate);
            if (saturdaySundayCount == 0) {
                Long day = DateDiff.getDaysDiff(fromDate, toDate);
                for (int value = 1; value <= day; value++) {
                    if (value == day) {
                        leaveDuration = leaveDuration - (16 * 60 * value) - 60;
                    }
                }
                return leaveDuration;
            } else if (saturdaySundayCount == 1) {
                if (DateDiff.getHour(fromDate) < stopMorningWorkTime) {
                    return leaveDuration - (60 * 16 * (workingDays));
                }
                return leaveDuration - (60 * 15 * (workingDays));
            } else {
                if (DateDiff.getHour(fromDate) < stopMorningWorkTime && DateDiff.getHour(toDate) <= stopMorningWorkTime
                        || DateDiff.getHour(fromDate) >= startAfternoonWorkTime && DateDiff.getHour(toDate) >= startAfternoonWorkTime) {
                    return leaveDuration - ((((20 * (saturdaySundayCount / 2)) + (24 * (saturdaySundayCount / 2))) * 60) + ((saturdaySundayCount / 2) * 30) + (60 * 16 * (workingDays - 1)));
                } else if (DateDiff.getHour(fromDate) >= startAfternoonWorkTime && DateDiff.getHour(toDate) <= stopMorningWorkTime) {
                    return leaveDuration - ((((20 * (saturdaySundayCount / 2)) + (24 * (saturdaySundayCount / 2))) * 60) + ((saturdaySundayCount / 2) * 30) + (60 * 16 * (workingDays)));
                }
                return leaveDuration - ((((20 * (saturdaySundayCount / 2)) + (24 * (saturdaySundayCount / 2))) * 60) + ((saturdaySundayCount / 2) * 30) + (60 * 16 * (workingDays - 1)) + 60);
            }
        }
    }

    public Long calculateExtraTimeTheFirstDayInMonth(Date toDate, Date theFirstDateOfMonth) {
        Long durationInLeaveApplicationWithDifferentMonthDb = DateDiff.getDateDiff(theFirstDateOfMonth, toDate, TimeUnit.MINUTES);
        Integer daysFromTheFirstDateInMonth = (DateDiff.getDate(toDate) - DateDiff.getDate(theFirstDateOfMonth));
        if (DateDiff.getHour(toDate) <= stopMorningWorkTime) {
            return (durationInLeaveApplicationWithDifferentMonthDb - ((16 * (daysFromTheFirstDateInMonth) * 60) + 480));
        }
        return (durationInLeaveApplicationWithDifferentMonthDb - ((16 * (daysFromTheFirstDateInMonth) * 60) + 540));
    }

    public Long calculateExtraTimeTheLastDayInMonth(Date fromDate, Date theLastDateOfMonth) {
        Long durationInLeaveApplicationWithDifferentMonthDb = DateDiff.getDateDiff(fromDate, theLastDateOfMonth, TimeUnit.MINUTES);
        Integer daysToTheLastDateInMonth = (DateDiff.getDate(theLastDateOfMonth) - DateDiff.getDate(fromDate));
        if (DateDiff.getHour(fromDate) <= stopMorningWorkTime) {
            return (durationInLeaveApplicationWithDifferentMonthDb - ((16 * (daysToTheLastDateInMonth)) + 420));
        }
        return (durationInLeaveApplicationWithDifferentMonthDb - ((16 * (daysToTheLastDateInMonth)) + 360));
    }


    public Long leaveDurationInMonthDb(String username, Date date) {
        LeaveApplication leaveApplicationWithDifferentMonthBeginDb = leaveApplicationRepository.getLeaveApplicationWithDifferentMonthToDate(username, DateDiff.getMonth(date) + 1);
        LeaveApplication leaveApplicationWithDifferentMonthEndDb = leaveApplicationRepository.getLeaveApplicationWithDifferentMonthFromDate(username, DateDiff.getMonth(date) + 1);
        Long leaveDurationWithTheSameMonth = getLeaveDurationWithSameMonthFormDb(username, DateDiff.getMonth(date) + 1);
        if (leaveApplicationWithDifferentMonthBeginDb == null && leaveApplicationWithDifferentMonthEndDb == null) {
            return leaveDurationWithTheSameMonth;
        } else if (leaveApplicationWithDifferentMonthBeginDb != null && leaveApplicationWithDifferentMonthEndDb == null) {
            Date theFirstDateOfMonth = DateDiff.getFirstDateOfMonth(date);
            Long extraTime = calculateExtraTimeTheFirstDayInMonth(leaveApplicationWithDifferentMonthBeginDb.getToDate(), theFirstDateOfMonth);
            return leaveDurationWithTheSameMonth + extraTime;
        } else if (leaveApplicationWithDifferentMonthBeginDb == null && leaveApplicationWithDifferentMonthEndDb != null) {
            Date theLastDateOfMonth = DateDiff.getLastDateOfMonth(date);
            Long extraTime = calculateExtraTimeTheLastDayInMonth(leaveApplicationWithDifferentMonthEndDb.getFromDate(), theLastDateOfMonth);
            return leaveDurationWithTheSameMonth + extraTime;
        } else {
            Date theFirstDateOfMonth = DateDiff.getFirstDateOfMonth(date);
            Long extraTimeTheFirstDayInMonth = calculateExtraTimeTheFirstDayInMonth(leaveApplicationWithDifferentMonthBeginDb.getToDate(), theFirstDateOfMonth);
            Date theLastDateOfMonth = DateDiff.getLastDateOfMonth(date);
            Long extraTimeTheLastDayInMonth = calculateExtraTimeTheLastDayInMonth(leaveApplicationWithDifferentMonthEndDb.getFromDate(), theLastDateOfMonth);
            return leaveDurationWithTheSameMonth + extraTimeTheFirstDayInMonth + extraTimeTheLastDayInMonth;
        }
    }

    public Boolean isLeaveDurationTimeOut(String username, Date fromDate, Date toDate, Long leaveTime, Long maxDurationInMonth) {
        if (DateDiff.getMonth(fromDate) == DateDiff.getMonth(toDate)) {
            Long leaveDuration = leaveDurationInMonthDb(username, fromDate);
            if ((leaveTime + leaveDuration) > maxDurationInMonth || leaveDuration >= maxDurationInMonth) {
                return true;
            }
            return false;
        } else {
            Long leaveDurationOfTheFirstMonthDb = leaveDurationInMonthDb(username, fromDate);
            Long leaveDurationOfTheSecondMonthDb = leaveDurationInMonthDb(username, toDate);
            Date theLastDateOfMonth = DateDiff.getLastDateOfMonth(fromDate);
            Long leaveDurationOfTheFirstMonth = calculateExtraTimeTheLastDayInMonth(fromDate, theLastDateOfMonth);
            Long leaveDurationOfTheSecondMonth = leaveTime - leaveDurationOfTheFirstMonth;
            if (leaveDurationOfTheFirstMonth > maxDurationInMonth || leaveDurationOfTheSecondMonth > maxDurationInMonth
                    || (leaveDurationOfTheFirstMonthDb + leaveDurationOfTheFirstMonth) > maxDurationInMonth
                    || (leaveDurationOfTheSecondMonthDb + leaveDurationOfTheSecondMonth) > maxDurationInMonth
                    || leaveDurationOfTheFirstMonthDb >= maxDurationInMonth
                    || leaveDurationOfTheSecondMonthDb >= maxDurationInMonth) {
                return true;
            }
            return false;
        }
    }

    @Override
    public List<LeaveApplication> getLeaveApplicationHistory() {
        String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
        return leaveApplicationRepository.getByUsername(employeeName);
    }

    @Override
    public LeaveApplication getById(Integer id) {
        Optional<LeaveApplication> leaveApplicationDb = leaveApplicationRepository.findById(id);
        if (!leaveApplicationDb.isPresent()) {
            throw new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_RECORD_NOT_FOUND);
        }
        return leaveApplicationDb.get();
    }

    @Override
    public LeaveApplication update(LeaveApplicationUpdateForm leaveApplicationUpdateForm) {
        LeaveApplication leaveApplication = getById(leaveApplicationUpdateForm.getId());
        if (leaveApplication.getStatus().equals("OK")) {
            throw new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_ACTION_ALREADY_TAKEN);
        } else {
            String employeeName = ExtractUserAuthentication.getCurrentUser().getUsername();
            Optional<LeavePolicy> leavePolicyDb = leavePolicyRepository.findById(leaveApplicationUpdateForm.getPolicyId());
            if (!leavePolicyDb.isPresent()) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_TYPE_NAME_INVALID);
            }
            Date dateModify = new Date();
            if ((leaveApplicationUpdateForm.getFromDate()).compareTo(leaveApplicationUpdateForm.getToDate()) > 0
                    || (leaveApplicationUpdateForm.getFromDate()).compareTo(dateModify) < 0) {
                throw new DataNotFoundException(ExceptionConstants.DATE_LEAVE_ILLEGAL);
            }
            if (isNotWorkingTime(leaveApplicationUpdateForm.getFromDate(), leaveApplicationUpdateForm.getToDate())
                    || isSaturdaySunday(leaveApplicationUpdateForm.getFromDate())
                    || isSaturdaySunday(leaveApplicationUpdateForm.getToDate())) {
                throw new DataNotFoundException(ExceptionConstants.NOT_WORKING_TIME);
            }
            Long leaveDuration = getLeaveDurationWithSameMonthFormDb(employeeName, DateDiff.getMonth(leaveApplicationUpdateForm.getFromDate()) + 1);
            if (leaveDuration >= leavePolicyDb.get().getDuration()) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_TIME_OUT);
            }
            Long leaveTime = leaveDuration(leaveApplicationUpdateForm.getFromDate(), leaveApplicationUpdateForm.getToDate());
            if (leaveTime > leavePolicyDb.get().getDuration() || (leaveTime + (leaveDuration - leaveApplication.getLeaveDuration()) > leavePolicyDb.get().getDuration())) {
                throw new DataNotFoundException(ExceptionConstants.LEAVE_DURATION_INVALID);
            }
            if (isDateLeaveExist(employeeName, leaveApplicationUpdateForm.getFromDate(), leaveApplicationUpdateForm.getToDate())) {
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
            } catch (Exception e) {
                throw new DataNotFoundException(ExceptionConstants.ERRORS);
            }
        }
    }

    @Override
    public Integer delete(LeaveApplication leaveApplication) {
        LeaveApplication leaveApplicationDb = getById(leaveApplication.getId());
        if (leaveApplicationDb.getStatus().equals("OK")) {
            throw new DataNotFoundException(ExceptionConstants.EMPLOYEE_LEAVE_ACTION_ALREADY_TAKEN);
        }
        leaveApplicationRepository.delete(leaveApplicationDb);
        return 0;
    }
}
