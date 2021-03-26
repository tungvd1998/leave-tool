package com.example.leave.repositories;

import com.example.leave.models.LeaveApplication;
import com.example.leave.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {
    List<LeaveApplication> getLeaveApplicationByUserId(Integer userId);

    @Query(value = " SELECT la " +
            " FROM LeaveApplication la " +
            " LEFT JOIN User u ON la.user = u.id" +
            " WHERE u.username = :username")
    List<LeaveApplication> getByUsername(@Param("username") String username);
}
