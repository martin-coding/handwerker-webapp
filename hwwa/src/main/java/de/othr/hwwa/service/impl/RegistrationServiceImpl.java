package de.othr.hwwa.service.impl;

import de.othr.hwwa.exceptions.EmailAlreadyUsedException;
import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.UserRegistrationDto;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.RoleRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.RegistrationServiceI;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl implements RegistrationServiceI {

    private final UserRepositoryI userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepositoryI roleRepository;
    private final CompanyRepositoryI companyRepository;

    RegistrationServiceImpl(UserRepositoryI userRepository, PasswordEncoder passwordEncoder, RoleRepositoryI roleRepository, CompanyRepositoryI companyRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public User registerNewOwner(UserRegistrationDto dto) {
        if (userRepository.findUserByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("Email wird bereits verwendet.");
        }

        User user = new User();
        setUserData(user, dto);

        // Es werden nur die Eingaben zur Firma überprüft.
        // Ob eine Firma mit gleichem Namen und Adresse bereits existiert ist für einen fehlerfreien Ablauf nicht relevant.
        Company company = new Company(dto.getCompanyName(), new Address(dto.getStreet(), dto.getCity(), dto.getPostalCode(), dto.getCountry()));
        companyRepository.save(company);
        user.setCompany(company);

        Role ownerRole = roleRepository.findByName("Owner")
                .orElseThrow(() -> new IllegalStateException("Es gibt keine Rolle mit der Bezeichnung: 'Owner'"));
        user.setRole(ownerRole);

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    @Override
    public User registerNewUser(UserRegistrationDto dto) {
        if (userRepository.findUserByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Es gibt bereits einen Nutzer mit dieser Email.");
        }

        User user = new User();
        setUserData(user, dto);

        Role employeeRole = roleRepository.findByName("Employee")
                .orElseThrow(() -> new IllegalStateException("Es gibt keine Rolle mit der Bezeichnung: 'Employee'"));
        user.setRole(employeeRole);


        return userRepository.save(user);
    }

    private void setUserData(User user, UserRegistrationDto dto) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
    }
}
