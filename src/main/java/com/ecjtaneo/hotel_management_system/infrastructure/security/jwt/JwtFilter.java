package com.ecjtaneo.hotel_management_system.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter) throws ServletException, IOException {
        String authorizationHeader = req.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || SecurityContextHolder.getContext().getAuthentication() != null) {
            filter.doFilter(req, res);
            return;
        }

        String jwt = authorizationHeader.substring(7);
        String userId;
        String userRole;

        try {
            Claims claims = jwtService.validate(jwt);
            userId = claims.getSubject();
            userRole = claims.get("role", String.class);

        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority(userRole)));

        SecurityContextHolder.getContext().setAuthentication(auth);
        filter.doFilter(req, res);

    }
}