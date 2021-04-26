package com.example.leave.api.forms;

import com.example.leave.infrastructure.constant.ErrorCode;
import com.example.leave.models.LeavePolicy;
import com.example.leave.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tungvd
 */
@Data
public class LeaveApplicationCreateForm {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date fromDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date toDate;

    private String reason;

    private Integer policyId;


}
