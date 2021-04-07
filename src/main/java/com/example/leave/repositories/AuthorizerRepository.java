package com.example.leave.repositories;

import com.example.leave.models.LeaveApplication;
import com.example.leave.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface AuthorizerRepository extends JpaRepository<Permission, Integer> {

    @Query(value = "SELECT p.api \n" +
            " FROM permissions p \n" +
            " INNER JOIN user_group_permission ugp ON ugp.permission_id = p.id \n" +
            " INNER JOIN user_group ug ON ug.id = ugp.user_group_id \n" +
            " INNER JOIN users u ON u.group_id = ug.id \n" +
            " WHERE u.username = :username" , nativeQuery = true)
    ArrayList<String> getApiByUsername(@Param("username") String username);
}

