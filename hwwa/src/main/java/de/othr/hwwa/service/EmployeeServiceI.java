package de.othr.hwwa.service;

import de.othr.hwwa.model.dto.NewEmployeeDto;
import de.othr.hwwa.model.Role;
import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeServiceI {
    public Page<User> getEmployeeList(int page, int size, String sort, String dir, String keyword);
    public UserDto getUserById(long id);
    public List<Role> findAllRoles();
    public void updateEmployee(long id, UserDto dto);
    public void deleteEmployee(long id);
    public void addNewEmployee(NewEmployeeDto dto);
    public boolean emailExists(String email);
}
