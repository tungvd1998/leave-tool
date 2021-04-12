package com.example.leave.api.forms;

import com.example.leave.models.LeavePolicy;
import com.example.leave.models.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tungvd
 */
@Data
public class LeaveApplicationCreateForm {

    private Date fromDate;

    private Date toDate;

    private String reason;

    private String status;

    private Integer policyId;

}
