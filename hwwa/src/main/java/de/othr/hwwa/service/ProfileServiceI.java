package de.othr.hwwa.service;

import de.othr.hwwa.model.dto.CompanyDto;
import de.othr.hwwa.model.dto.ProfileDto;

public interface ProfileServiceI {
    public ProfileDto getCurrentProfileDto();
    public CompanyDto getCurrentCompanyDto();
    public void updateProfile(ProfileDto profile);
    public void updateCompany(CompanyDto company);
}
