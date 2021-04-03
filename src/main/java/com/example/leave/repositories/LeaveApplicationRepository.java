package com.example.leave.repositories;

import com.example.leave.models.LeaveApplication;
import com.example.leave.models.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {
    List<LeaveApplication> getLeaveApplicationByUserId(Integer userId);

    @Query(value = " SELECT la " +
            " FROM LeaveApplication la " +
            " LEFT JOIN User u ON la.user = u.id" +
            " WHERE u.username = :username")
    List<LeaveApplication> getByUsername(@Param("username") String username);

    @Query(value = " SELECT SUM(leave_duration) " +
            " FROM leave_applications la " +
            " INNER JOIN users u ON la.user_id = u.id" +
            " WHERE u.username = :username AND MONTH (from_date) = :month", nativeQuery = true)

    Integer calculateLeaveDurationByUsername(@Param("username") String username, @Param("month") Integer month);

    @Query(value = " SELECT COUNT(*) > 0 " +
            " FROM leave_applications la " +
            " INNER JOIN users u ON la.user_id = u.id" +
            " WHERE u.username = :username " +
            " AND la.from_date =  STR_TO_DATE(:fromDate, '%Y-%m-%d %H:%i:%s')" +
            " OR la.to_date =  STR_TO_DATE(:toDate, '%Y-%m-%d %H:%i:%s')", nativeQuery = true)
    Integer getFromDate(@Param("username") String username, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

}
