package de.othr.hwwa.service.impl;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.exceptions.EqualUserException;
import de.othr.hwwa.exceptions.UserDoesNotExistsException;
import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.NewEmployeeDto;
import de.othr.hwwa.model.dto.UserDto;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.RoleRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.EmployeeServiceI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl implements EmployeeServiceI {

    private final UserRepositoryI userRepository;
    private final RoleRepositoryI roleRepository;
    private final CompanyRepositoryI companyRepository;

    public EmployeeServiceImpl(UserRepositoryI userRepository, RoleRepositoryI roleRepository, CompanyRepositoryI companyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Page<User> getEmployeeList(int page, int size, String sort, String dir, String keyword){
        User loggedInUser = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLoggedInUser();
        Company company = loggedInUser.getCompany();
        Sort.Direction direction =
                dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sortOrder = Sort.by(direction, sort);
        if ("lastName".equals(sort)) {
            sortOrder = Sort.by(direction, "lastName").and(Sort.by(direction, "firstName"));
        }

        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByCompanyIdAndActiveTrueAndKeyword(company.getId(), keyword, pageRequest);
        }
        return userRepository.findByCompanyIdAndActiveTrue(company.getId(), pageRequest);
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
        User newUser  = new User();
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword());
        newUser.setHourlyRate(dto.getHourlyRate());

        Role role = roleRepository.getRoleByName(dto.getRoleName());
        if (role == null) {
            // Kann nur auftreten, wenn schädlicher Nutzer versucht das System zu sabotieren, daher ist es nicht notwendig dies extra in der GUI anzuzeigen.
            throw new IllegalArgumentException("Role nicht gefunden");
        }
        newUser.setRole(roleRepository.getRoleByName(dto.getRoleName()));

        User currentUser = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new UserDoesNotExistsException("Nutzer mit dieser Id existiert nicht"));
        Company company = companyRepository.getCompanyById(currentUser.getId());
        newUser.setCompany(company);

        // Angelegter Nutzer wird zunächst wie gelöschter Account betrachtet. Durch Registrierung wird der Account aktiv.
        newUser.setActive(false);
        userRepository.save(newUser);
    }

    @Override
    public boolean emailExists(String email){
        return userRepository.findUserByEmailIgnoreCase(email).isPresent();
    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new UserDto();
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
