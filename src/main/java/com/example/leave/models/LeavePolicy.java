package com.example.leave.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "leave_policy")
public class LeavePolicy implements Serializable {
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
