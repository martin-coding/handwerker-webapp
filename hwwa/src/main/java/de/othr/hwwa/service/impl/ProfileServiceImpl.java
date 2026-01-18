package de.othr.hwwa.service.impl;
import de.othr.hwwa.exceptions.MissingPasswordException;
import de.othr.hwwa.model.Address;
import de.othr.hwwa.model.Company;
import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.CompanyDto;
import de.othr.hwwa.model.dto.ProfileDto;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.ProfileServiceI;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class ProfileServiceImpl extends SecurityServiceImpl implements ProfileServiceI {
    private UserRepositoryI userRepository;
    private CompanyRepositoryI companyRepository;
    private PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserRepositoryI userRepository, CompanyRepositoryI companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
    }

    @Override
    public ProfileDto getCurrentProfileDto() {
        User currentUser = getCurrentUser();
        return new ProfileDto(currentUser.getFirstName(), currentUser.getLastName(), currentUser.getEmail(), currentUser.getPhoneNumber(), currentUser.getCreatedAt().toLocalDate());
    }

    @Override
    public CompanyDto getCurrentCompanyDto() {
        Company company = getCurrentCompany();
        Address address = company.getAddress();
        return new CompanyDto(address.getStreet(), address.getCity(), address.getPostalCode(), address.getCountry(), company.getName());
    }

    @Override
    public void updateProfile(ProfileDto profile) {
        User currentUser = getCurrentUser();

        updateIfChanged(currentUser::getFirstName, currentUser::setFirstName, profile.getFirstName());
        updateIfChanged(currentUser::getLastName, currentUser::setLastName, profile.getLastName());
        updateIfChanged(currentUser::getEmail, currentUser::setEmail, profile.getEmail());
        updateIfChanged(currentUser::getPhoneNumber, currentUser::setPhoneNumber, profile.getPhoneNumber());

        if ((profile.getOldPassword() != null) ^ (profile.getNewPassword() != null)){
            throw new MissingPasswordException("Fehlendes neues oder altes Passwort");
        }
        if ((profile.getNewPassword() != null) && (profile.getOldPassword() != null)){
            if (passwordEncoder.matches(profile.getOldPassword(), currentUser.getPassword())){
                currentUser.setPassword(passwordEncoder.encode(profile.getNewPassword()));
            }
            else{
                throw new IllegalArgumentException("Altes Passwort ist falsch");
            }
        }
        userRepository.save(currentUser);
    }

    @Override
    public void updateCompany(CompanyDto company) {
        Company currentCompany = getCurrentCompany();
        Address address = currentCompany.getAddress();
        updateIfChanged(currentCompany::getName, currentCompany::setName, company.getCompanyName());
        updateIfChanged(address::getStreet, address::setStreet, company.getStreet());
        updateIfChanged(address::getCity, address::setCity, company.getCity());
        updateIfChanged(address::getPostalCode, address::setPostalCode, company.getPostalCode());
        updateIfChanged(address::getCountry, address::setCountry, company.getCountry());


        companyRepository.save(currentCompany);
    }

    private void updateIfChanged(Supplier<String> getter, Consumer<String> setter, String newValue) {
        if (!Objects.equals(getter.get(), newValue)) {
            setter.accept(newValue);
        }
    }

}
