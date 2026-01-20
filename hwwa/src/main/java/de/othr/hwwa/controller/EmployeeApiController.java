package de.othr.hwwa.controller;

import de.othr.hwwa.model.dto.UserDto;
import de.othr.hwwa.exceptions.UserDoesNotExistsException;
import de.othr.hwwa.model.User;
import de.othr.hwwa.service.EmployeeServiceI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApiController {

    private final EmployeeServiceI employeeService;

    public EmployeeApiController(EmployeeServiceI employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public PagedModel<UserDto> getAllEmployees(
            @PageableDefault(size = 20, sort = "lastName")Pageable pageable,
            @RequestParam(required = false) String keyword) {
        Page<UserDto> page = employeeService.getEmployeeList(pageable, keyword).map(this::toDto);
        return new PagedModel<>(page);
    }

    @GetMapping("/{employeeId}")
    public UserDto getEmployee(@PathVariable long employeeId) {
        try {
            return employeeService.getUserById(employeeId);
        } catch (UserDoesNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoleName(),
                user.getHourlyRate(),
                user.getCreatedAt().toLocalDate()
        );
    }
}
