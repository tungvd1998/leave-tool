package com.example.leave.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "leave_applications")
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date fromDate;
    private Date toDate;
    private String reason;
    private Date created;
    private String status;
    private Integer policyId;
    private Integer userId;
}
