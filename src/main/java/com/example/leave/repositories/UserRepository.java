package com.example.leave.repositories;

import com.example.leave.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query(value = "SELECT u.id FROM User u WHERE u.username = ?1")
    Integer findIdByUsername(String username);

    @Query(value = "SELECT o FROM User o WHERE o.id = ?1")
    User getUser(Integer userId);
}
