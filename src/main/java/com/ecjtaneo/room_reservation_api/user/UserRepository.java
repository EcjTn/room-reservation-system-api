package com.ecjtaneo.room_reservation_api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecjtaneo.room_reservation_api.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByUsername(String username);
    public Optional<User> findByUsername(String username);
}
