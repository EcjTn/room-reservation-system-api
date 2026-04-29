package com.ecjtaneo.room_reservation_api.booking;

import com.ecjtaneo.room_reservation_api.booking.dto.BookingCreationDto;
import com.ecjtaneo.room_reservation_api.booking.dto.BookingPublicResponseDto;
import com.ecjtaneo.room_reservation_api.booking.model.Booking;
import com.ecjtaneo.room_reservation_api.booking.model.BookingStatus;
import com.ecjtaneo.room_reservation_api.booking.model.PaymentStatus;
import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import com.ecjtaneo.room_reservation_api.common.exception.ResourceNotFoundException;
import com.ecjtaneo.room_reservation_api.common.exception.ValidationException;
import com.ecjtaneo.room_reservation_api.room.RoomService;
import com.ecjtaneo.room_reservation_api.room.model.Room;
import com.ecjtaneo.room_reservation_api.user.UserService;
import com.ecjtaneo.room_reservation_api.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserService userService;

    private BigDecimal calculateTotalAmount(BigDecimal pricePerNight, LocalDate startDate, LocalDate endDate) {
        return pricePerNight
                .multiply(new BigDecimal(endDate.toEpochDay() - startDate.toEpochDay()));
    }

    private boolean validateDates(LocalDate startDate, LocalDate endDate) {
        return startDate.isBefore(endDate);
    }

    public MessageResponseDto createBooking(BookingCreationDto bookingCreationDto, Long userId) {
        if(!validateDates(bookingCreationDto.startDate(), bookingCreationDto.endDate())) throw new ValidationException("Invalid dates");
        
        int updateRoom = roomService.setRoomBookedIfAvailable(bookingCreationDto.roomNumber());
        if(updateRoom <= 0) throw new ValidationException("Room not available");

        Room room = roomService.getRoomReferenceByRoom(bookingCreationDto.roomNumber());
        User userRef = userService.getUserReference(userId);

        BigDecimal totalAmount = calculateTotalAmount(room.getPricePerNight(), bookingCreationDto.startDate(), bookingCreationDto.endDate());

        Booking booking = new Booking();

        booking.setRoom(room);
        booking.setUser(userRef);

        booking.setStartDate(bookingCreationDto.startDate());
        booking.setEndDate(bookingCreationDto.endDate());
        booking.setTotalAmount(totalAmount);

        booking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(booking);
        roomService.setRoomBooked(room.getRoomNumber());

        return new MessageResponseDto("Booking successfully created.");
    }

    @Transactional
    public MessageResponseDto deleteBooking(Long id) {
        if(bookingRepository.deleteBookingById(id) <= 0) throw new ResourceNotFoundException("Booking not found");
        return new MessageResponseDto("Booking successfully deleted.");
    }

    @Transactional
    public MessageResponseDto cancelBooking(Long id) {
        Booking booking = bookingRepository.findByIdAndStatus(id, BookingStatus.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);

        roomService.setRoomAvailable(booking.getRoom().getRoomNumber());

        return new MessageResponseDto("Booking successfully cancelled.");
    }

    public boolean canCancelBooking(Long id, Long userId) {
        return bookingRepository.existsBookingByIdAndUserId(id, userId);
    }

    public List<BookingPublicResponseDto> getRecentBookings() {
        return bookingRepository.findTop10OrderByIdDesc();
    }

    public List<BookingPublicResponseDto> getRecentBookingsBefore(Long lastSeenId) {
        return bookingRepository.findTop10OrderByIdDescBefore(lastSeenId);
    }

    @Cacheable(value = "bookings", key = "#userId")
    public List<BookingPublicResponseDto> getBookingsByUserId(Long userId) {
        return bookingRepository.findTop10ByUserIdOrderByIdDesc(userId);
    }

    public List<BookingPublicResponseDto> getBookingsBeforeByUserId(Long lastSeenId, Long userId) {
        return bookingRepository.findTop10ByIdLessThanAndUserIdOrderByIdDesc(lastSeenId, userId);
    }

    public long getBookingsCount() {
        return bookingRepository.count();
    }

    public long getActiveBookingsCount() {
        return bookingRepository.countByStatus(BookingStatus.CONFIRMED);
    }

    public long getCompletedBookingsCount() {
        return bookingRepository.countByStatus(BookingStatus.COMPLETED);
    }

    public BigDecimal calculateTotalRevenue() {
        return bookingRepository.sumTotalAmountByStatus(PaymentStatus.PAID);
    }

    public BigDecimal calculateRevenueBetweenDates(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.sumByStatusAndCreatedAtBetween(PaymentStatus.PAID, start, end);
    }

    //Admins only operations
    @Transactional
    public MessageResponseDto confirmBooking(Long id) {
        Booking booking = bookingRepository.findByIdAndStatus(id, BookingStatus.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        roomService.setRoomOccupied(booking.getRoom().getRoomNumber());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentStatus(PaymentStatus.PAID);

        return new MessageResponseDto("Booking successfully confirmed.");
    }

    @Transactional
    public MessageResponseDto completeBooking(Long id) {
        Booking booking = bookingRepository.findByIdAndStatus(id, BookingStatus.CONFIRMED)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        roomService.setRoomAvailable(booking.getRoom().getRoomNumber());
        booking.setStatus(BookingStatus.COMPLETED);

        return new MessageResponseDto("Booking successfully completed.");
    }

}
