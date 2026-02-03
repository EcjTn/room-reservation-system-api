package com.ecjtaneo.hotel_management_system.auth;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecjtaneo.hotel_management_system.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByToken(String token);

    @Query("""
            SELECT rt
            FROM RefreshToken rt
            JOIN FETCH rt.user
            WHERE token = :token
                AND rt.expires_at > :now
            """)
    public Optional<RefreshToken> findValidToken(@Param("token")String token, @Param("now") LocalDateTime now);
}
