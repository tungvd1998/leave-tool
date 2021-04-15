package com.example.leave.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@Table(name = "work_time")
public class WorkTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer startWorkTime;
    private Integer stopMorningWorkTime;
    private Integer startAfternoonWorkTime;
    private Integer endWorkTime;

}
