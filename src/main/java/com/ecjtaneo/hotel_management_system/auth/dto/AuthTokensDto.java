package com.ecjtaneo.hotel_management_system.auth.dto;

public record AuthTokensDto(
        String accessToken,
        String refreshToken
) {}