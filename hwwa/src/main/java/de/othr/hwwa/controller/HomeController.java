package de.othr.hwwa.controller;
import de.othr.hwwa.config.MyUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = {"/home", "/"})
    public String home(Model model) {
        return "homepage";
    }
}