package com.example.leave.repositories;

import com.example.leave.models.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Integer> {

    @Query(value = "SELECT * FROM leave_policy lp \n" +
            "WHERE lp.id = :id", nativeQuery = true)
    LeavePolicy getLeavePolicy(Integer id);
}