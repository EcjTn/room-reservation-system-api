package com.ecjtaneo.hotel_management_system.booking;

import com.ecjtaneo.hotel_management_system.booking.model.Booking;
import com.ecjtaneo.hotel_management_system.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Modifying
    @Query("DELETE FROM Booking b WHERE b.id = :id")
    public int deleteBookingById(Long id);

    @Modifying
    @Query("""
            UPDATE Booking b
            SET b.status = :status
            WHERE b.id = :id
            """)
    public int updateStatusById(Long id, BookingStatus status);

    @EntityGraph(attributePaths = {"user", "room"})
    public Optional<Booking> findWithUserAndRoomById(Long id);
}
