package com.example.leave.api.view;

import com.example.leave.infrastructure.constant.ErrorCode;
import com.example.leave.models.LeaveApplication;
import lombok.Data;

/**
 * @author tungvd
 */
@Data
public class LeaveApplicationView {
    private LeaveApplication leaveApplication;

    private Boolean isSuccess = Boolean.TRUE;

    private String errorCode = "";

    private String message = "";

    public LeaveApplicationView(LeaveApplication leaveApplication) {
        this.isSuccess = Boolean.TRUE;
        this.leaveApplication = leaveApplication;
    }

    public LeaveApplicationView(ErrorCode errorCode) {
        this.isSuccess = Boolean.FALSE;
        this.errorCode = errorCode.name();
        this.message = errorCode.name();
    }

    public LeaveApplicationView(ErrorCode errorCode, String message) {
        this.isSuccess = Boolean.FALSE;
        this.errorCode = errorCode.name();
        this.message = message;
    }
}
