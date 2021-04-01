package com.example.leave.repositories;

import com.example.leave.models.LeaveApplication;
import com.example.leave.models.Role;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppAuthorizerRepository extends JpaRepository<LeaveApplication, Integer> {

    @Query(value = "SELECT r.name" +
            " FROM roles r\n" +
            " INNER JOIN user_role ur ON ur.role_id = r.id\n" +
            " INNER JOIN users u ON u.id = ur.user_id\n" +
            " Where u.username = :username" , nativeQuery = true)
    String  findNameRole(@Param("username") String username);
}

