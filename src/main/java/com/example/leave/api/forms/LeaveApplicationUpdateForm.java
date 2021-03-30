package com.example.leave.api.forms;

import lombok.Data;

import java.util.Date;

/**
 * @author tungvd
 */
@Data
public class LeaveApplicationUpdateForm {
    private Integer id;

    private Date fromDate;

    private Date toDate;

    private String reason;

    private String status;

    private Integer policyId;

}
