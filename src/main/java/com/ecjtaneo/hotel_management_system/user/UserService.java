package com.ecjtaneo.hotel_management_system.user;


import java.util.Optional;

import com.ecjtaneo.hotel_management_system.user.dto.UserCreationCommandDto;
import com.ecjtaneo.hotel_management_system.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(String username) {
        return userRepository.findByUsername(username);
    }
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public void createUser(UserCreationCommandDto newUser) {
        User user = new User();
        user.setUsername(newUser.username());
        user.setPassword(newUser.password());
        userRepository.save(user);
    }

}
