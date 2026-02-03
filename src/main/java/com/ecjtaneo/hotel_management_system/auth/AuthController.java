package com.ecjtaneo.hotel_management_system.auth;


import com.ecjtaneo.hotel_management_system.auth.dto.AuthLoginDto;
import com.ecjtaneo.hotel_management_system.auth.dto.AuthRegisterDto;
import com.ecjtaneo.hotel_management_system.auth.dto.AuthTokensDto;
import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto register(@RequestBody @Valid AuthRegisterDto user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponseDto> login(@RequestBody @Valid AuthLoginDto user) {
        AuthTokensDto tokens = authService.login(user);

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, tokens.refreshToken())
                .httpOnly(true)
                .sameSite("Lax")
                .secure(false) //true in production
                .build();

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new MessageResponseDto(tokens.accessToken()));
    }


    @PostMapping("/refresh")
    public ResponseEntity<MessageResponseDto> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        AuthTokensDto tokens = authService.refreshToken(refreshToken);

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponseDto(tokens.accessToken()));
    }

}
