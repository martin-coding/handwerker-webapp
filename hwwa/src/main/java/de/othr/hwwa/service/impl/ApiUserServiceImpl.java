package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.jwt.ApiUser;
import de.othr.hwwa.repository.ApiUserRepositoryI;
import de.othr.hwwa.service.ApiUserServiceI;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiUserServiceImpl implements ApiUserServiceI {
    private ApiUserRepositoryI apiUserRepository;
    private PasswordEncoder passwordEncoder;

    public ApiUserServiceImpl(ApiUserRepositoryI apiUserRepository, PasswordEncoder passwordEncoder) {
        this.apiUserRepository = apiUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiUser authenticate(String email, String password){
        ApiUser user = apiUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("ApiNutzer mit dieser Mail nicht gefunden"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Ungültiges Passwort");
        }

        return user;
    }

}
