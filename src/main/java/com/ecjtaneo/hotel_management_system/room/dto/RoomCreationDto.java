package com.ecjtaneo.hotel_management_system.room.dto;


import com.ecjtaneo.hotel_management_system.room.model.RoomStatus;
import com.ecjtaneo.hotel_management_system.room.model.RoomType;
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
