package com.ecjtaneo.room_reservation_api.report.dto;

import java.math.BigDecimal;

public record ReportsSummaryDto(
        long totalBookings,
        long totalAvailableRooms,
        long totalActiveBookings,
        long totalCompletedBookings,
        long totalRooms,
        BigDecimal totalRevenue
) {}
