package de.othr.hwwa.startup;

import de.othr.hwwa.model.Authority;
import de.othr.hwwa.model.Role;
import de.othr.hwwa.model.User;
import de.othr.hwwa.repository.AuthorityRepositoryI;
import de.othr.hwwa.repository.RoleRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryI roleRepository;
    private final AuthorityRepositoryI authorityRepository;
    private final UserRepositoryI userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            RoleRepositoryI roleRepository,
            AuthorityRepositoryI authorityRepository,
            UserRepositoryI userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if authorities exist
        Authority tasks = authorityRepository.findByName("tasks")
                .orElseGet(() -> authorityRepository.save(new Authority("tasks")));
        Authority basic = authorityRepository.findByName("basic")
                .orElseGet(() -> authorityRepository.save(new Authority("basic")));
        Authority createUser = authorityRepository.findByName("create_user")
                .orElseGet(() -> authorityRepository.save(new Authority("create_user")));

        // Check if role exists
        Role employee = roleRepository.findByName("Employee")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("Employee");
                    r.setAuthorities(Set.of(tasks, basic));
                    return roleRepository.save(r);
                });
        Role manager = roleRepository.findByName("Manager")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("Manager");
                    r.setAuthorities(Set.of(tasks, basic));
                    return roleRepository.save(r);
                });
        Role owner = roleRepository.findByName("Owner")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("Owner");
                    r.setAuthorities(Set.of(tasks, createUser, basic));
                    return roleRepository.save(r);
                });


        //Check if dummy user exists
        User user1 = userRepository.findUserByUserName("thomastest")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("thomas");
                    user.setLastName("test");
                    user.setUserName("thomastest");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(owner);
                    return userRepository.save(user);
                });
    }
}
