package com.ecjtaneo.hotel_management_system.user.dto;

public record UserCreationCommandDto(
        String username,
        String password
) {}
