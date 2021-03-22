package com.example.leave.repositories;

import com.example.leave.models.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {
    List<LeaveApplication> getLeaveApplicationByUserId(Integer userId);
}
