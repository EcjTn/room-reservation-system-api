package com.ecjtaneo.room_reservation_api.booking.dto;

import com.ecjtaneo.room_reservation_api.booking.model.BookingStatus;
import com.ecjtaneo.room_reservation_api.booking.model.PaymentStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingPublicResponseDto(
        Long id,
        Long userId,
        String roomNumber,
        BookingStatus status,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalAmount,
        PaymentStatus paymentStatus
) implements Serializable {}
