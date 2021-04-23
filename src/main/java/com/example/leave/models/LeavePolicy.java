package com.example.leave.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "leave_policy")
public class LeavePolicy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer duration;
    private Long maxDurationInMonth;
    private Date created;
    private String content;
    private Date fromDate;
    private Date toDate;
    private Date modified;
    private Integer creatorId;

}
