package com.ecjtaneo.room_reservation_api.auth;


import com.ecjtaneo.room_reservation_api.auth.dto.AuthLoginDto;
import com.ecjtaneo.room_reservation_api.auth.dto.AuthRegisterDto;
import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto register(@RequestBody @Valid AuthRegisterDto user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto login(@RequestBody @Valid AuthLoginDto user, HttpServletRequest request) {
        Authentication authentication = authService.login(user);

        HttpSession session = request.getSession(true);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        return new MessageResponseDto("Successfully logged in.");
    }

    @DeleteMapping("/logout")
    public MessageResponseDto logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new MessageResponseDto("Successfully logged out.");
    }

}
