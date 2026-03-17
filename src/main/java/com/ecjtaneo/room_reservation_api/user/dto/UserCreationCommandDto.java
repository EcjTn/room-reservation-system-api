package com.ecjtaneo.room_reservation_api.user.dto;

public record UserCreationCommandDto(
        String username,
        String password
) {}
