package com.ecjtaneo.room_reservation_api.auth;


import com.ecjtaneo.room_reservation_api.auth.dto.AuthRegisterDto;
import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto register(@RequestBody @Valid AuthRegisterDto user) {
        return authService.register(user);
    }

}
