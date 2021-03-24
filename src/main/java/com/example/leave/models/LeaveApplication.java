package com.example.leave.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "leave_applications")
public class LeaveApplication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date fromDate;
    private Date toDate;
    private String reason;
    private Date created;
    private String status;

//    private Integer userId;
//    private Integer policyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
//    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private LeavePolicy leavePolicy;
    //    private Integer policyId;
}
