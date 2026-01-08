package de.othr.hwwa.service.impl;
import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.ProfileDto;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.ProfileServiceI;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl extends BaseServiceImpl implements ProfileServiceI {
    private UserRepositoryI userRepository;

    public ProfileServiceImpl(UserRepositoryI userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public ProfileDto getCurrentProfile() {
        User currentUser = userRepository.getUsersById(getCurrentUserId());
        return new ProfileDto(currentUser.getFirstName(), currentUser.getLastName(), currentUser.getEmail(), currentUser.getPhoneNumber());
    }
}
