package de.othr.hwwa.service;


import de.othr.hwwa.model.jwt.ApiUser;

public interface ApiUserServiceI {
    public ApiUser authenticate(String userName, String password);
}
