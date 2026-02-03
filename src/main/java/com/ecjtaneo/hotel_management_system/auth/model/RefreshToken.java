package com.ecjtaneo.hotel_management_system.auth.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ecjtaneo.hotel_management_system.user.model.User;

import lombok.Data;

@Data
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    private final LocalDateTime created_at = LocalDateTime.now();
    private LocalDateTime expires_at = LocalDateTime.now().plusDays(30);
}
