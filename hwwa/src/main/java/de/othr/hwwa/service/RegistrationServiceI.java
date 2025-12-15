package de.othr.hwwa.service;

import de.othr.hwwa.model.User;
import de.othr.hwwa.model.UserRegistrationDto;

public interface RegistrationServiceI {
    public User registerNewUser(UserRegistrationDto dto);
}
