package com.ecjtaneo.hotel_management_system.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthRegisterDto(
        @NotNull
        @Size(min = 5, max = 30)
        String username,

        @NotNull
        @Size(min = 8, max = 200)
        String password,

        @NotNull
        String recaptchaToken
) {}
