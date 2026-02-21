package com.ecjtaneo.hotel_management_system.booking.model;

import com.ecjtaneo.hotel_management_system.room.model.Room;
import com.ecjtaneo.hotel_management_system.user.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings", uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_room", columnNames = {"user_id", "room_id"})
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BigDecimal totalAmount;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
