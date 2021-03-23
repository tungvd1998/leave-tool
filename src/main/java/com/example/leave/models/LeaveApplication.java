package com.example.leave.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @ManyToOne
    @JoinColumn(name = "user_id") // thông qua khóa ngoại address_id
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
//    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "policy_id") // thông qua khóa ngoại address_id
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private LeavePolicy leavePolicy;
    //    private Integer policyId;
}
