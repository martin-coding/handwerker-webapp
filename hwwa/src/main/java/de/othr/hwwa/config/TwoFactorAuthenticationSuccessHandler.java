package de.othr.hwwa.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import java.io.IOException;

public class TwoFactorAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        if (userDetails.isTwoFactorEnabled()) {
            response.sendRedirect("/2fa");
        } else {
            response.sendRedirect("/");
        }
    }
}
