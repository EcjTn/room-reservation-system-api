package com.ecjtaneo.hotel_management_system.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecjtaneo.hotel_management_system.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByUsername(String username);
    public Optional<User> findByUsername(String username);
}
