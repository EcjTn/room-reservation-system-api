package com.ecjtaneo.hotel_management_system.auth;

import com.ecjtaneo.hotel_management_system.auth.dto.AuthLoginDto;
import com.ecjtaneo.hotel_management_system.auth.dto.AuthRegisterDto;
import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import com.ecjtaneo.hotel_management_system.common.exception.ResourceConflictException;
import com.ecjtaneo.hotel_management_system.common.exception.ValidationException;
import com.ecjtaneo.hotel_management_system.infrastructure.security.recaptcha.RecaptchaService;
import com.ecjtaneo.hotel_management_system.user.UserService;
import com.ecjtaneo.hotel_management_system.user.dto.UserCreationCommandDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RecaptchaService recaptchaService;
    private AuthenticationManager authenticationManager;

    public AuthService(
        UserService userService,
        PasswordEncoder passwordEncoder,
        RecaptchaService recaptchaService,
        AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.recaptchaService = recaptchaService;
        this.authenticationManager = authenticationManager;
    }

    public MessageResponseDto register(AuthRegisterDto user) {
        if(userService.isUsernameTaken(user.username())) throw new ResourceConflictException("User already exists");
        if(!recaptchaService.validate(user.recaptchaToken())) throw new ValidationException("Recaptcha validation failed");

        String hashPassword = passwordEncoder.encode(user.password());
        userService.createUser(new UserCreationCommandDto(user.username(), hashPassword));

        return new MessageResponseDto("Successfully registered.");
    }

    public Authentication login(AuthLoginDto user){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        return authenticationManager.authenticate(authToken);
    }


}
