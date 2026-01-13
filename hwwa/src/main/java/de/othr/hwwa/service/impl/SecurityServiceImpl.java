package de.othr.hwwa.service.impl;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.model.Company;
import de.othr.hwwa.model.User;
import de.othr.hwwa.service.SecurityServiceI;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityServiceImpl implements SecurityServiceI {

    public User getCurrentUser(){
        return ((MyUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getLoggedInUser();
    }

    public Company getCurrentCompany(){
        return getCurrentUser().getCompany();
    }

    @Override
    public long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
