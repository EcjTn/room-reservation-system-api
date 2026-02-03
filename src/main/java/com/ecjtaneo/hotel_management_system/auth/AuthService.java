package com.ecjtaneo.hotel_management_system.auth;

import com.ecjtaneo.hotel_management_system.auth.dto.AuthLoginDto;
import com.ecjtaneo.hotel_management_system.auth.dto.AuthRegisterDto;
import com.ecjtaneo.hotel_management_system.auth.dto.AuthTokensDto;
import com.ecjtaneo.hotel_management_system.auth.model.RefreshToken;
import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import com.ecjtaneo.hotel_management_system.common.exception.ResourceConflictException;
import com.ecjtaneo.hotel_management_system.common.exception.ResourceNotFoundException;
import com.ecjtaneo.hotel_management_system.common.exception.ValidationException;
import com.ecjtaneo.hotel_management_system.infrastructure.security.jwt.JwtService;
import com.ecjtaneo.hotel_management_system.infrastructure.security.UserDetailsImpl;
import com.ecjtaneo.hotel_management_system.infrastructure.security.recaptcha.RecaptchaService;
import com.ecjtaneo.hotel_management_system.user.UserService;
import com.ecjtaneo.hotel_management_system.user.dto.UserCreationCommandDto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RecaptchaService recaptchaService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private RefreshTokenRepository refreshTokenRepository;

    public AuthService(
        UserService userService,
        PasswordEncoder passwordEncoder,
        RecaptchaService recaptchaService,
        JwtService jwtService,
        AuthenticationManager authenticationManager,
        RefreshTokenRepository refreshTokenRepository
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.recaptchaService = recaptchaService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public MessageResponseDto register(AuthRegisterDto user) {
        if(userService.isUsernameTaken(user.username())) throw new ResourceConflictException("User already exists");
        if(!recaptchaService.validate(user.recaptchaToken())) throw new ValidationException("Recaptcha validation failed");

        String hashPassword = passwordEncoder.encode(user.password());
        userService.createUser(new UserCreationCommandDto(user.username(), hashPassword));

        return new MessageResponseDto("Successfully registered.");
    }

    public AuthTokensDto login(AuthLoginDto user){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        Authentication authentication = authenticationManager.authenticate(authToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String userId = userDetails.getId().toString();
        String userRole = userDetails.getRole();
        String accessToken = jwtService.generate(userId, userRole);

        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setUser(userDetails.getUser());
        refreshTokenObj.setToken(UUID.randomUUID().toString());

        refreshTokenRepository.save(refreshTokenObj);

        return new AuthTokensDto(accessToken, refreshTokenObj.getToken());
    }

    @Transactional
    public AuthTokensDto refreshToken(String oldRefreshToken) {
        RefreshToken tokenRecord = refreshTokenRepository.findValidToken(oldRefreshToken, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Refresh  token"));

        var tokenExpiry = tokenRecord.getExpires_at();
        if(tokenExpiry.isBefore(LocalDateTime.now())) throw new ValidationException("Invalid Refresh token");

        //Rotating tokens rt and at
        String userId = tokenRecord.getUser().getId().toString();
        String userRole = tokenRecord.getUser().getRole().name();

        String accessToken = jwtService.generate(userId, userRole);
        String refreshToken = UUID.randomUUID().toString();

        //update
        tokenRecord.setToken(refreshToken);
        tokenRecord.setExpires_at(LocalDateTime.now().plusDays(30));

        return new AuthTokensDto(accessToken, refreshToken);
    }


}
