package de.othr.hwwa.controller;

import de.othr.hwwa.model.UserRegistrationDto;
import de.othr.hwwa.service.RegistrationServiceI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationServiceI registrationService;

    public RegistrationController(RegistrationServiceI registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") UserRegistrationDto dto) {
        registrationService.registerNewUser(dto);
        return "redirect:/login"; // After registration, redirect to login-page
    }
}