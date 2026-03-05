package com.ecjtaneo.hotel_management_system.booking.model;

import com.ecjtaneo.hotel_management_system.room.model.Room;
import com.ecjtaneo.hotel_management_system.user.model.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_user_id", columnList = "room_id"),
        @Index(name = "idx_user_id", columnList = "user)id")
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalAmount;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
