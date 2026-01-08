package de.othr.hwwa.service;

import de.othr.hwwa.model.EmailDetails;
import de.othr.hwwa.model.User;

import java.util.concurrent.Future;

public interface EmailServiceI{
    void sendRegistrationEmail(User user, String notEncryptedPassword);

}
