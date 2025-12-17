package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.User;
import de.othr.hwwa.model.UserRegistrationDto;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.RegistrationServiceI;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationServiceI {

    private final UserRepositoryI userRepository;
    private final PasswordEncoder passwordEncoder;

    RegistrationServiceImpl(UserRepositoryI userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUser(UserRegistrationDto dto) {
        if (userRepository.findUserByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with same email already exists");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUserName(dto.getFirstName() + dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }
}
