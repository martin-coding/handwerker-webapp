package de.othr.hwwa.controller;

import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.NewEmployeeDto;
import de.othr.hwwa.model.dto.UserDto;
import de.othr.hwwa.service.EmployeeServiceI;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeServiceI employeeService;

    public EmployeeController(EmployeeServiceI employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public String showEmployeePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "lastName") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model
    ) {
        Sort.Direction direction =
                dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sortOrder = Sort.by(direction, sort);
        if ("lastName".equals(sort)) {
            sortOrder = Sort.by(direction, "lastName").and(Sort.by(direction, "firstName"));
        }
        PageRequest pageable = PageRequest.of(page, size, sortOrder);
        Page<User> employeePage = employeeService.getEmployeeList(pageable, keyword);

        model.addAttribute("employees", employeePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("keyword", keyword);

        return "employee_management/employee";
    }

    @GetMapping("/edit/{id}")
    public String showEmployeeUpdatePage(@PathVariable int id, Model model) {
        UserDto user = employeeService.getUserById(id);
        List<Role> roles = employeeService.findAllRoles();

        model.addAttribute("employee", user);
        model.addAttribute("roles", roles);
        model.addAttribute("employeeChanged", user);
        return  "employee_management/employee_edit";
    }


    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable int id, @Valid @ModelAttribute("employeeChanged") UserDto dto, BindingResult result, Model model) {
        if (result.hasErrors()){
            List<Role> roles = employeeService.findAllRoles();
            model.addAttribute("roles", roles);
            model.addAttribute("employee", employeeService.getUserById(id));
            return "employee_management/employee_edit";
        }
        employeeService.updateEmployee(id, dto);
        return   "redirect:/employee";
    }


    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mitarbeiter wurde erfolgreich gelöscht.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mitarbeiter konnte nicht gelöscht werden: " + e.getMessage());
        }
        return "redirect:/employee"; // redirect back to employee list
    }


    @GetMapping("/add")
    public String showEmployeeAddPage(Model model) {
        List<Role> roles = employeeService.findAllRoles();
        NewEmployeeDto newEmployee = new NewEmployeeDto();
        newEmployee.setRoleName("Employee");
        model.addAttribute("roles", roles);
        model.addAttribute("newEmployee", newEmployee);
        return "employee_management/employee_add";
    }

    @PostMapping("/add")
    public String addNewEmployee(@Valid @ModelAttribute("newEmployee") NewEmployeeDto dto, BindingResult result, Model model) {
        if (!dto.getPassword().equals(dto.getPassword_check())) {
            result.rejectValue("password_check", null, "Passwort stimmt nicht überein");
        }
        if (employeeService.emailExists(dto.getEmail())) {
            result.rejectValue("email", null, "Email bereits vergeben");
        }
        if (result.hasErrors()) {
            return "employee_management/employee_add";
        }

        try{
            employeeService.addNewEmployee(dto);
        } catch(IllegalArgumentException e){}
        return "redirect:/employee";
    }
}
