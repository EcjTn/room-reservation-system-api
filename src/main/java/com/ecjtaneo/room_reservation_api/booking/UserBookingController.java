package com.ecjtaneo.room_reservation_api.booking;

import com.ecjtaneo.room_reservation_api.booking.dto.BookingPublicResponseDto;
import com.ecjtaneo.room_reservation_api.infrastructure.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserBookingController {
    private final BookingService bookingService;

    @GetMapping("/me/bookings")
    public List<BookingPublicResponseDto> showBookings(
            @RequestParam(name = "cursor", required = false) Long cursor,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long userId = userDetails.getUserId();
        if(cursor == null) return bookingService.getBookingsByUserId(userId);
        return bookingService.getBookingsBeforeByUserId(cursor, userId);
    }

    @GetMapping("/{id}/bookings")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<BookingPublicResponseDto> showBookingsByUserId(
            @RequestParam(name = "cursor", required = false) Long cursor,
            @PathVariable("id") Long userId
    ) {
        if(cursor == null) return bookingService.getBookingsByUserId(userId);
        return bookingService.getBookingsBeforeByUserId(cursor, userId);
    }

}
