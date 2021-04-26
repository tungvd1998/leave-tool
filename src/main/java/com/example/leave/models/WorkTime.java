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
    private Integer begin;
    private Integer lunchBreak;
    private Integer backToWork;
    private Integer end;

}
