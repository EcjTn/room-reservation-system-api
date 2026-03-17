package com.ecjtaneo.room_reservation_api.auth.dto;

import jakarta.validation.constraints.NotNull;

public record AuthLoginDto(
        @NotNull
        String username,
        @NotNull
        String password
) {}
