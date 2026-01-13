package de.othr.hwwa.service;

import de.othr.hwwa.model.Company;
import de.othr.hwwa.model.User;

public interface SecurityServiceI {
    public long getCurrentUserId();
    public Company getCurrentCompany();
    public User getCurrentUser();
}
