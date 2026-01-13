package de.othr.hwwa.controller;

import de.othr.hwwa.exceptions.MissingPasswordException;
import de.othr.hwwa.model.dto.CompanyDto;
import de.othr.hwwa.model.dto.ProfileDto;
import de.othr.hwwa.service.ProfileServiceI;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    ProfileServiceI profileService;

    public ProfileController(ProfileServiceI profileService) {
        this.profileService = profileService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // trims strings and converts empty strings to null
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/profile")
    public String showProfilPage(Model model) {
        model.addAttribute("profile", profileService.getCurrentProfileDto());
        model.addAttribute("company", profileService.getCurrentCompanyDto());
        return "profile";
    }

    @PostMapping("/profile/employee/edit")
    public String updateEmployeeProfile(@Valid @ModelAttribute("profile") ProfileDto profile, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("company", profileService.getCurrentCompanyDto());
            return "profile";
        }

        try {
            profileService.updateProfile(profile);
        } catch (MissingPasswordException e) {
            result.rejectValue("oldPassword", null, e.getMessage());
            result.rejectValue("newPassword", null, e.getMessage());
            model.addAttribute("company", profileService.getCurrentCompanyDto());
            return "profile"; // go back to profile page
        } catch (IllegalArgumentException e) {
            result.rejectValue("oldPassword", null, e.getMessage());
            model.addAttribute("company", profileService.getCurrentCompanyDto());
            return "profile"; // go back to profile page
        }
        return "redirect:/profile?success";
    }

    @PostMapping("/profile/company/edit")
    public String updateCompany(@Valid @ModelAttribute("company") CompanyDto company, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("profile", profileService.getCurrentProfileDto());
            return "profile";
        }
        profileService.updateCompany(company);
        return "redirect:/profile?success";
    }
}