package com.ecjtaneo.room_reservation_api.report;

import com.ecjtaneo.room_reservation_api.booking.BookingService;
import com.ecjtaneo.room_reservation_api.report.dto.ReportsSummaryDto;
import com.ecjtaneo.room_reservation_api.room.RoomService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReportService {
    private final BookingService bookingService;
    private final RoomService roomService;

    public ReportService(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    public ReportsSummaryDto getReportsSummary() {
        long totalBookings = bookingService.getBookingsCount();
        long totalActiveBookings = bookingService.getActiveBookingsCount();
        long totalCompletedBookings = bookingService.getCompletedBookingsCount();
        long totalAvailableRooms = roomService.getAvailableRoomsCount();
        long totalRooms = roomService.getRoomsCount();
        BigDecimal totalRevenue = bookingService.calculateTotalRevenue();

        return new ReportsSummaryDto(
                totalBookings,
                totalAvailableRooms,
                totalActiveBookings,
                totalCompletedBookings,
                totalRooms,
                totalRevenue
        );
    }

    public BigDecimal getTotalRevenueByDateRange(LocalDateTime start, LocalDateTime end) {
        return bookingService.calculateRevenueBetweenDates(start, end);
    }
}
