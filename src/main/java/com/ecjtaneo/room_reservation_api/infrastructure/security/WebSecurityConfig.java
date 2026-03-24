package com.ecjtaneo.room_reservation_api.infrastructure.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .cors(cors -> {})

                .csrf(c -> c
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/auth/register"))

                .formLogin(f -> f
                        .loginProcessingUrl("/auth/login")
                        .successHandler((req, res, auth) -> {res.setStatus(HttpServletResponse.SC_OK);})
                        .failureHandler((req, res, auth) -> {res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);})
                )

                .httpBasic(h -> h.disable())

                .logout(l -> l
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler((req, res, auth) -> {res.setStatus(HttpServletResponse.SC_OK);}))

                .authorizeHttpRequests(a -> a
                        .requestMatchers("/auth/logout").authenticated()
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/bookings").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/bookings/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/confirm/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/complete/**").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/**").hasAnyAuthority("ADMIN", "GUEST")

                        .requestMatchers(HttpMethod.GET, "/rooms/**").hasAnyAuthority("ADMIN", "GUEST")
                        .requestMatchers("/rooms/**").hasAuthority("ADMIN")

                        .requestMatchers("/reports/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
