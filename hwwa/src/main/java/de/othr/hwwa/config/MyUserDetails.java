package de.othr.hwwa.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.othr.hwwa.model.Role;
import de.othr.hwwa.repository.UserRepositoryI;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.othr.hwwa.model.Authority;
import de.othr.hwwa.model.User;

public class MyUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
    private Role role;
    private User loggedInUser;


    public MyUserDetails(User user) {
        this.loggedInUser = user;
        this.email = user.getEmail();
        this.password= user.getPassword();
        System.out.println("password of the user is="+password);
        System.out.println("email of the user is="+this.email);
        this.active = user.isActive();

        Role userRole = user.getRole();
        this.authorities = new ArrayList<>();
        if (userRole != null){
            for (Authority authority : userRole.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(authority.getName()));
            }
        }


    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

}