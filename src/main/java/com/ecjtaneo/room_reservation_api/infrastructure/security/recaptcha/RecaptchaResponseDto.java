package com.ecjtaneo.room_reservation_api.infrastructure.security.recaptcha;

public record RecaptchaResponseDto(
        boolean success,
        String hostname
) {}
