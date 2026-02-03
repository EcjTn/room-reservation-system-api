package com.ecjtaneo.hotel_management_system.infrastructure.security.recaptcha;

public record RecaptchaResponseDto(
        boolean success,
        String hostname
) {}
