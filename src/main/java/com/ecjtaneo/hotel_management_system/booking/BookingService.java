package com.ecjtaneo.hotel_management_system.booking;

import com.ecjtaneo.hotel_management_system.booking.dto.BookingCreationDto;
import com.ecjtaneo.hotel_management_system.booking.model.Booking;
import com.ecjtaneo.hotel_management_system.booking.model.BookingStatus;
import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import com.ecjtaneo.hotel_management_system.room.RoomService;
import com.ecjtaneo.hotel_management_system.room.model.Room;
import com.ecjtaneo.hotel_management_system.user.UserService;
import com.ecjtaneo.hotel_management_system.user.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserService userService;

    public BookingService(BookingRepository bookingRepository, RoomService roomService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.userService = userService;
    }

    public MessageResponseDto createBooking(BookingCreationDto bookingCreationDto, Long userId) {
        Room room = roomService.findAvailableRoom(bookingCreationDto.roomNumber());
        roomService.markRoomBooked(room.getRoomNumber());
        User userRef = userService.getUserReference(userId);

        BigDecimal totalAmount = room.getPricePerNight()
                .multiply(new BigDecimal(
                        bookingCreationDto.endDate().toEpochDay() - bookingCreationDto.startDate().toEpochDay()
                ));

        Booking booking = new Booking();

        booking.setRoom(room);
        booking.setUser(userRef);

        booking.setStartDate(bookingCreationDto.startDate());
        booking.setEndDate(bookingCreationDto.endDate());

        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(booking);

        return new MessageResponseDto("Booking successfully created.");
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteBookingById(id);
    }

    public int updateBookingStatus(Long id, BookingStatus status) {
        return bookingRepository.updateStatusById(id, status);
    }

}
