package com.example.leave.repositories;

import com.example.leave.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query(value = "SELECT id from users u where username = :username", nativeQuery = true)
    Integer findIdByUsername(String username);
}
