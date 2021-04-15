package com.example.leave.repositories;

import com.example.leave.models.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface WorkTimeRepository extends JpaRepository<WorkTime, Integer> {
    @Query(value ="SELECT * FROM work_time",nativeQuery = true)
    WorkTime getWorkTime();
}
