package com.example.leave.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    @Column(length = 60)
    private String password;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
//    private Integer status; khong co trong db
    private java.util.Date created;
    private Date modified;
    private String imageProfile;
    private Integer gender;
    private Date birthday;
    private Date hireDate;

    private Integer deptId;
    private Integer groupId;

}
