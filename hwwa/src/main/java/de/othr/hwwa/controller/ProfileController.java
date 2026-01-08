package de.othr.hwwa.controller;

import de.othr.hwwa.model.dto.NewEmployeeDto;
import de.othr.hwwa.model.dto.ProfileDto;
import de.othr.hwwa.service.ProfileServiceI;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    ProfileServiceI profileService;

    public ProfileController(ProfileServiceI profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public String showProfilPage(Model model) {
        model.addAttribute("profileDto", profileService.getCurrentProfile());
        return "profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfilPage(@Valid @ModelAttribute("profileDto") NewEmployeeDto dto, BindingResult result, Model model) {

        return "redirect:/profile";
    }
}