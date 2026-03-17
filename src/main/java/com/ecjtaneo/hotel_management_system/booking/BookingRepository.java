package com.ecjtaneo.hotel_management_system.booking;

import com.ecjtaneo.hotel_management_system.booking.dto.BookingPublicResponseDto;
import com.ecjtaneo.hotel_management_system.booking.model.Booking;
import com.ecjtaneo.hotel_management_system.booking.model.BookingStatus;
import com.ecjtaneo.hotel_management_system.booking.model.PaymentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @EntityGraph(attributePaths = {"room"})
    public Optional<Booking> findByIdAndStatus(Long id, BookingStatus status);

    public boolean existsBookingByIdAndUserId(Long id, Long userId);

    public long countByStatus(BookingStatus status);

    @Query("""
        SELECT new com.ecjtaneo.hotel_management_system.booking.dto.BookingPublicResponseDto(
            b.id,
            u.id,
            r.roomNumber,
            b.status,
            b.startDate,
            b.endDate,
            b.totalAmount,
            b.paymentStatus
        )
        FROM Booking b
        JOIN b.room r
        JOIN b.user u
        ORDER BY b.id DESC
        LIMIT 10
    """)
    public List<BookingPublicResponseDto> findTop10OrderByIdDesc();
    

    @Query("""
        SELECT new com.ecjtaneo.hotel_management_system.booking.dto.BookingPublicResponseDto(
            b.id,
            u.id,
            r.roomNumber,
            b.status,
            b.startDate,
            b.endDate,
            b.totalAmount,
            b.paymentStatus
        )
        FROM Booking b
        JOIN b.room r
        JOIN b.user u
        WHERE b.id < :lastSeenId
        ORDER BY b.id DESC
        LIMIT 10
    """)
    public List<BookingPublicResponseDto> findTop10OrderByIdDescBefore(Long lastSeenId);
    

    @Query("""
        SELECT COALESCE(SUM(b.totalAmount), 0)
        FROM Booking b
        WHERE b.paymentStatus = :paymentStatus
        AND b.createdAt BETWEEN :start AND :end
    """)
    public BigDecimal sumByStatusAndCreatedAtBetween(PaymentStatus paymentStatus, LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT COALESCE(SUM(b.totalAmount), 0)
        FROM Booking b
        WHERE b.paymentStatus = :paymentStatus
    """)
    public BigDecimal sumTotalAmountByStatus(PaymentStatus paymentStatus);

    @Query("""
        SELECT new com.ecjtaneo.hotel_management_system.booking.dto.BookingPublicResponseDto(
            b.id,
            u.id,
            r.roomNumber,
            b.status,
            b.startDate,
            b.endDate,
            b.totalAmount,
            b.paymentStatus
        )
        FROM Booking b
        JOIN b.room r
        JOIN b.user u
        WHERE u.id = :userId
        ORDER BY b.id DESC
        LIMIT 10
    """)
    List<BookingPublicResponseDto> findTop10ByUserIdOrderByIdDesc(@Param("userId") Long userId);

    @Query("""
        SELECT new com.ecjtaneo.hotel_management_system.booking.dto.BookingPublicResponseDto(
            b.id,
            u.id,
            r.roomNumber,
            b.status,
            b.startDate,
            b.endDate,
            b.totalAmount,
            b.paymentStatus
         )
         FROM Booking b
         JOIN b.room r
         JOIN b.user u
         WHERE u.id = :userId
         AND b.id < :lastSeenId 
         ORDER BY b.id DESC
         LIMIT 10
            """)
    public List<BookingPublicResponseDto> findTop10ByIdLessThanAndUserIdOrderByIdDesc(@Param("lastSeenId") Long lastSeenId, @Param("userId") Long userId);
}