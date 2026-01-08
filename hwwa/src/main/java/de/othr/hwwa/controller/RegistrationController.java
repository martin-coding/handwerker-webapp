package de.othr.hwwa.controller;

import de.othr.hwwa.exceptions.EmailAlreadyUsedException;
import de.othr.hwwa.model.dto.UserRegistrationDto;
import de.othr.hwwa.service.RegistrationServiceI;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationServiceI registrationService;

    public RegistrationController(RegistrationServiceI registrationService) {
        this.registrationService = registrationService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // trims strings and converts empty strings to null
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping("/user")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto dto) {
        registrationService.registerNewUser(dto);
        return "redirect:/login"; // After registration, redirect to login-page
    }

    @PostMapping
    public String registerOwner(@Valid @ModelAttribute("user") UserRegistrationDto dto, BindingResult result) {
        if (result.hasErrors()){
            return "registration";
        }
        try {
            registrationService.registerNewOwner(dto);
        } catch (EmailAlreadyUsedException e) {
            // Add an error message to the model to display in the form
            result.rejectValue("email", null, "Diese E-Mail ist bereits vergeben");
            return "registration"; // go back to registration page
        }
        return "redirect:/login"; // After registration, redirect to login-page
    }
}


