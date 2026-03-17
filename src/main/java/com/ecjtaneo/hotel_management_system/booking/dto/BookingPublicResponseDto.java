package com.ecjtaneo.hotel_management_system.booking.dto;

import com.ecjtaneo.hotel_management_system.booking.model.BookingStatus;
import com.ecjtaneo.hotel_management_system.booking.model.PaymentStatus;

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
