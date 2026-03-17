package com.ecjtaneo.room_reservation_api.room.dto;


import com.ecjtaneo.room_reservation_api.room.model.RoomStatus;
import com.ecjtaneo.room_reservation_api.room.model.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RoomCreationDto(
        @NotBlank
        String roomNumber,

        @NotNull
        RoomStatus status,

        @NotNull
        RoomType type,

        @NotNull
        BigDecimal price_per_night
) {}
