package com.example.leave.repositories;

import com.example.leave.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query(value = "SELECT u.id FROM User u WHERE u.username = ?1")
    Integer findIdByUsername(String username);

    @Query(value = "SELECT o FROM User o WHERE o.id = ?1")
    User getUser(Integer userId);

    @Query(value = "SELECT * FROM users u \n" +
            "WHERE u.username = :username", nativeQuery = true)
    User getUser(@Param("username") String username);

    @Query(value = "SELECT * FROM users u \n" +
            " WHERE u.id = (SELECT d.leader_id \n" +
            " FROM users u \n" +
            " INNER JOIN departments d ON u.dept_id = d.id \n" +
            " WHERE u.username = :username)", nativeQuery = true)
    User getUserLeader(@Param("username") String username);

}
