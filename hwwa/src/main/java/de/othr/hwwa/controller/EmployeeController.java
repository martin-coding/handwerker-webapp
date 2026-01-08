package de.othr.hwwa.controller;

import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.NewEmployeeDto;
import de.othr.hwwa.model.dto.UserDto;
import de.othr.hwwa.service.EmployeeServiceI;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeServiceI employeeService;

    public EmployeeController(EmployeeServiceI employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    public String showEmployeePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "lastName") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model
    ) {
        Page<User> employeePage = employeeService.getEmployeeList(page, size, sort, dir, keyword);

        model.addAttribute("employees", employeePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("keyword", keyword);

        return "employee_management/employee";
    }

    @GetMapping("/employee/edit/{id}")
    public String showEmployeeUpdatePage(@PathVariable int id, Model model) {
        UserDto user = employeeService.getUserById(id);
        List<Role> roles = employeeService.findAllRoles();

        model.addAttribute("employee", user);
        model.addAttribute("roles", roles);
        model.addAttribute("employeeChanged", user);
        return  "employee_management/employee_edit";
    }


    @PostMapping("/employee/edit/{id}")
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


    @PostMapping("/employee/delete/{id}")
    public String deleteEmployee(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mitarbeiter wurde erfolgreich gelöscht.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mitarbeiter konnte nicht gelöscht werden: " + e.getMessage());
        }
        return "redirect:/employee"; // redirect back to employee list
    }


    @GetMapping("/employee/add")
    public String showEmployeeAddPage(Model model) {
        List<Role> roles = employeeService.findAllRoles();
        NewEmployeeDto newEmployee = new NewEmployeeDto();
        newEmployee.setRoleName("Employee");
        model.addAttribute("roles", roles);
        model.addAttribute("newEmployee", newEmployee);
        return "employee_management/employee_add";
    }

    @PostMapping("/employee/add")
    public String addNewEmployee(@Valid @ModelAttribute("newEmployee") NewEmployeeDto dto, BindingResult result, Model model) {
        // TODO PasswordDto und ProfileDto getrennt an Page schicken und erhalten. Fehlerüberprüfung durchführen und User updaten
        /*
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
        */
        return "redirect:/employee";
    }
}
