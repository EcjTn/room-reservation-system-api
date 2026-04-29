package com.ecjtaneo.room_reservation_api.user;


import java.util.Optional;

import com.ecjtaneo.room_reservation_api.user.dto.UserCreationCommandDto;
import com.ecjtaneo.room_reservation_api.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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

    public User getUserReference(Long id) {
        return userRepository.getReferenceById(id);
    }

}
