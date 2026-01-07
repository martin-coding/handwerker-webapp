package de.othr.hwwa.controller;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.model.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController{

    @ModelAttribute("currentUser")
    public UserDto currentUser(@AuthenticationPrincipal MyUserDetails user) {
        //return current user
        if(user==null){
            return new UserDto();
        }
        return new UserDto(user.getLoggedInUser().getFirstName(), user.getLoggedInUser().getLastName(), user.getLoggedInUser().getRole().getName()); // returns UserDto with empty strings if not logged in
    }

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
