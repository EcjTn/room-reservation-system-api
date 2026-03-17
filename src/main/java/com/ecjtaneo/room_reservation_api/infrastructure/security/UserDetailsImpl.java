package com.ecjtaneo.room_reservation_api.infrastructure.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecjtaneo.room_reservation_api.user.model.User;

public class UserDetailsImpl implements UserDetails, Serializable {
    private final String userRole;
    private final Long userId;
    private final String username;
    private final String userPassword;

    public UserDetailsImpl(User user) {
        this.username = user.getUsername();
        this.userPassword = user.getPassword();
        this.userRole = user.getRole().name();
        this.userId = user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole));
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Long getUserId() {
        return this.userId;
    }
}
