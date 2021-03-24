package com.example.leave.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private Integer status;
    private java.util.Date created;
    private Date modified;
    private String imageProfile;
    private Integer gender;
    private Date birthday;
    private Date hireDate;
    private Integer deptId;
    private Integer groupId;

    @ManyToMany(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinTable(	name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    private Set<Role> roles;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
//    private Set<LeaveApplication> leaveApplications;

}
