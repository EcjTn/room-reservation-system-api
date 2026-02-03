package com.ecjtaneo.hotel_management_system.auth.dto;

import jakarta.validation.constraints.NotNull;

public record AuthLoginDto(
        @NotNull
        String username,
        @NotNull
        String password
) {}
