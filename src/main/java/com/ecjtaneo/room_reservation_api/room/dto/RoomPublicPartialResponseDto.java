package com.ecjtaneo.room_reservation_api.room.dto;

import com.ecjtaneo.room_reservation_api.room.model.RoomType;

import java.io.Serializable;

public record RoomPublicPartialResponseDto (
        Long id,
        String roomNumber,
        RoomType type
) implements Serializable {}
