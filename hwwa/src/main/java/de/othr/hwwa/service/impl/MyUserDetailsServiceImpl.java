package de.othr.hwwa.service.impl;

import de.othr.hwwa.service.MyUserDetailsServiceI;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.model.User;
import de.othr.hwwa.repository.UserRepositoryI;


@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsServiceI {

    private final UserRepositoryI userRepository;

    public MyUserDetailsServiceImpl(UserRepositoryI userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new MyUserDetails(user);
    }
}
