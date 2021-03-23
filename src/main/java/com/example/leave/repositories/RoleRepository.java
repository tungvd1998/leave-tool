package com.example.leave.repositories;

import com.example.leave.models.ERole;
import com.example.leave.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
