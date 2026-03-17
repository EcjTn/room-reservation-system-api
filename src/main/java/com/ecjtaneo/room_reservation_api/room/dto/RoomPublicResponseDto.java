package com.ecjtaneo.room_reservation_api.room.dto;

import com.ecjtaneo.room_reservation_api.room.model.RoomStatus;
import com.ecjtaneo.room_reservation_api.room.model.RoomType;

import java.io.Serializable;
import java.math.BigDecimal;

public record RoomPublicResponseDto(
        Long id,
        String roomNumber,
        RoomStatus status,
        RoomType type,
        BigDecimal pricePerNight
) implements Serializable {
}
