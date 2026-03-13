package com.ecjtaneo.hotel_management_system.report.dto;

import java.math.BigDecimal;

public record ReportsSummaryDto(
        long totalBookings,
        long totalAvailableRooms,
        long totalActiveBookings,
        long totalCompletedBookings,
        long totalRooms,
        BigDecimal totalRevenue
) {}
