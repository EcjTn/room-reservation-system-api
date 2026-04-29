package com.ecjtaneo.room_reservation_api.room;

import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomCreationDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomPublicPartialResponseDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomPublicResponseDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomUpdateDto;
import com.ecjtaneo.room_reservation_api.room.model.RoomStatus;
import com.ecjtaneo.room_reservation_api.room.model.RoomType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/{roomNumber}")
    public RoomPublicResponseDto showRoomByRoomNumber(@PathVariable String roomNumber) {
        return roomService.findRoomByRoomNumber(roomNumber);
    }

    @GetMapping
    public List<RoomPublicPartialResponseDto> showRooms(@RequestParam(name = "cursor", required = false) Long cursor) {
        if(cursor == null) return roomService.getRooms();
        return roomService.getRoomsBefore(cursor);
    }

    @GetMapping("/filters")
    public List<RoomPublicResponseDto> showRoomsWithFilter(
            @RequestParam(name = "status", required = false, defaultValue = "AVAILABLE") RoomStatus status,
            @RequestParam(name = "type", required = false, defaultValue = "SINGLE") RoomType type,
            @RequestParam(name = "cursor", required = false) Long cursor
    ) {
        if(cursor == null) return roomService.getRoomsByFilters(status, type);
        return roomService.getRoomsByFiltersBefore(status, type, cursor);
    }

    @PostMapping
    public MessageResponseDto createNewRoom(@RequestBody @Valid RoomCreationDto roomCreationDto) {
        return roomService.createNewRoom(roomCreationDto);
    }

    @DeleteMapping("/{roomNumber}")
    public MessageResponseDto deleteRoom(@PathVariable String roomNumber) {
        return roomService.deleteByRoomNumber(roomNumber);
    }

    @PutMapping("/{roomNumber}")
    public MessageResponseDto updateRoom(@PathVariable String roomNumber, @RequestBody @Valid RoomUpdateDto roomUpdateDto) {
        return roomService.updateRoom(roomNumber, roomUpdateDto);
    }

}
