package de.othr.hwwa.service.impl;

import de.othr.hwwa.exceptions.EqualUserException;
import de.othr.hwwa.exceptions.UserDoesNotExistsException;
import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.NewEmployeeDto;
import de.othr.hwwa.model.dto.UserDto;
import de.othr.hwwa.repository.RoleRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.EmailServiceI;
import de.othr.hwwa.service.EmployeeServiceI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl extends SecurityServiceImpl implements EmployeeServiceI {

    private final UserRepositoryI userRepository;
    private final RoleRepositoryI roleRepository;
    private final EmailServiceI emailService;
    private final PasswordEncoder passwordEncoder;



    public EmployeeServiceImpl(UserRepositoryI userRepository, RoleRepositoryI roleRepository, EmailServiceI emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<User> getEmployeeList(Pageable pageable, String keyword){
        Long companyId = getCurrentCompanyId();
    
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByCompanyIdAndActiveTrueAndKeyword(companyId, keyword, pageable);
        }
        return userRepository.findByCompanyIdAndActiveTrue(companyId, pageable);
    }

    @Override
    public void updateEmployee(long id, UserDto dto){
        boolean changed = false;
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserDoesNotExistsException("Dieser Nutzer existiert nicht");
        }
        if (! user.getRole().getName().equals(dto.getRoleName())){
            user.setRole(roleRepository.getRoleByName(dto.getRoleName()));
            changed = true;
        }
        if (user.getHourlyRate() != dto.getHourlyRate()){
            user.setHourlyRate(dto.getHourlyRate());
            changed = true;
        }
        if (changed){
            userRepository.save(user);
        }
    }

    @Override
    public void deleteEmployee(long id) {
        if (id == getCurrentUserId()) {
            throw new EqualUserException("Nutzer darf sich nicht selbst löschen");

        }
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserDoesNotExistsException("Dieser Nutzer existiert nicht");
        }
        // User wird auf inaktiv gesetzt und nicht komplett gelöscht, um Inkonsistenzen in der Datenbank zu vermeiden.
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void addNewEmployee(NewEmployeeDto dto) {
        Role role = roleRepository.getRoleByName(dto.getRoleName());
        if (role == null) {
            // Kann nur auftreten, wenn schädlicher Nutzer versucht das System zu sabotieren, daher ist es nicht notwendig dies extra in der GUI anzuzeigen.
            throw new IllegalArgumentException("Role nicht gefunden");
        }
        User newUser  = new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getHourlyRate(), role, getCurrentCompany(), LocalDateTime.now());


        if (newUser.getCreatedAt() == null) {
            newUser.setCreatedAt(LocalDateTime.now());
        }
        userRepository.save(newUser);

        emailService.sendRegistrationEmail(newUser, dto.getPassword());
    }

    @Override
    public boolean emailExists(String email){
        return userRepository.findUserByEmailIgnoreCase(email).isPresent();
    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserDoesNotExistsException(null);
        }
        else{
            return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getRole().getName(), user.getHourlyRate());
        }
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
