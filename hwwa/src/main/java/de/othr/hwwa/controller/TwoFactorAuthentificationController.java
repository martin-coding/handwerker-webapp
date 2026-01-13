package de.othr.hwwa.controller;

import de.othr.hwwa.exceptions.TwoFactorAuthentificationException;
import de.othr.hwwa.model.dto.TwoFactorAuthentificationDto;
import de.othr.hwwa.service.TwoFactorServiceI;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/2fa")
public class TwoFactorAuthentificationController {

    TwoFactorServiceI twoFactorService;

    public TwoFactorAuthentificationController(TwoFactorServiceI twoFactorService) {
        this.twoFactorService = twoFactorService;
    }

    @GetMapping
    public String inputTwoFactorCode(Model model) {
        model.addAttribute("tFA", new TwoFactorAuthentificationDto());
        return "2fa/2fa_login";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute("tFA") TwoFactorAuthentificationDto tFA, BindingResult result) {
        if(result.hasErrors()) {
            return "2fa/2fa_login";
        }
        try{
            twoFactorService.verify2FA(tFA.getSecret());
        }
        catch(TwoFactorAuthentificationException e){
            result.rejectValue("secret", null, e.getMessage());
            return "2fa/2fa_login";
        }

        return  "redirect:/";
    }

    @GetMapping(value="/qr", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getQrCode(Authentication auth) {
        return twoFactorService.generateQrCode();
    }

    @PostMapping("/complete")
    public String complete2FASetup(@Valid @ModelAttribute("tFA") TwoFactorAuthentificationDto tFA, BindingResult result) {
        if(result.hasErrors()) {
            return "2fa/2fa_setup";
        }
        try{
            twoFactorService.complete2FASetup(tFA.getSecret());
        }
        catch(TwoFactorAuthentificationException e){
            result.rejectValue("secret", null, e.getMessage());
            return "2fa/2fa_setup";
        }

        return  "redirect:/profile?success";
    }

    @GetMapping("/init")
    public String setup2fa(Model model) {
        try{
            twoFactorService.setup2FA();
        }
        catch(IllegalStateException e){
            return "redirect:/profile?error";
        }
        model.addAttribute("tFA", new TwoFactorAuthentificationDto());
        return "2fa/2fa_setup";
    }
}
