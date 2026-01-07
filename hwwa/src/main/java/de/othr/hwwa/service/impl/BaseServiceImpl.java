package de.othr.hwwa.service.impl;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.service.BaseServiceI;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseServiceImpl implements BaseServiceI {
    public long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails currentUser = (MyUserDetails) auth.getPrincipal();
        return currentUser.getLoggedInUser().getId();
    }
}
