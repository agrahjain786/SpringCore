package com.techlabs.app.repository;

import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

}
