package com.example.leave.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "leave_policy")
public class LeavePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer duration;
    private Date created;
    private String content;
    private Date fromDate;
    private Date toDate;
    private Integer creatorId;
}
