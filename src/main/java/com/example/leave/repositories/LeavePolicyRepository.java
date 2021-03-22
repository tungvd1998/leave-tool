package com.example.leave.repositories;

import com.example.leave.models.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Integer> {

}
