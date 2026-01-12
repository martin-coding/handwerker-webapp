package de.othr.hwwa.startup;

import de.othr.hwwa.model.*;
import de.othr.hwwa.repository.AuthorityRepositoryI;
import de.othr.hwwa.repository.ClientRepositoryI;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.RoleRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryI roleRepository;
    private final AuthorityRepositoryI authorityRepository;
    private final UserRepositoryI userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepositoryI companyRepository;
    private final ClientRepositoryI clientRepository;

    public DataInitializer(
            RoleRepositoryI roleRepository,
            AuthorityRepositoryI authorityRepository,
            UserRepositoryI userRepository,
            PasswordEncoder passwordEncoder,
            CompanyRepositoryI companyRepository,
            ClientRepositoryI clientRepository
    ) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
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
        Authority manageEmployees = authorityRepository.findByName("manage_employees")
                .orElseGet(() -> authorityRepository.save(new Authority("manage_employees")));
        Authority manageClients = authorityRepository.findByName("manage_clients")
                .orElseGet(() -> authorityRepository.save(new Authority("manage_clients")));

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
                    r.setAuthorities(Set.of(tasks, basic, manageClients));
                    return roleRepository.save(r);
                });
        Role owner = roleRepository.findByName("Owner")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("Owner");
                    r.setAuthorities(Set.of(tasks, createUser,manageEmployees, basic, manageClients));
                    return roleRepository.save(r);
                });


        Company company = new Company("abc", new Address("abc", "abc", "123", "abc"));
        companyRepository.save(company);
        Company differentCompany = new Company("def", new Address("def", "def", "456", "def"));
        companyRepository.save(differentCompany);

        //Check if dummy user exists
        User user1 = userRepository.findUserByEmailIgnoreCase("thomas.test@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Thomas");
                    user.setLastName("Test");
                    user.setEmail("thomas.test@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(owner);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        //Check if dummy user exists
        User user2 = userRepository.findUserByEmailIgnoreCase("sarah.mueller@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Sarah");
                    user.setLastName("Mueller");
                    user.setEmail("sarah.mueller@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        //Check if dummy user exists
        User user3 = userRepository.findUserByEmailIgnoreCase("lea.meier@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Lea");
                    user.setLastName("Meier");
                    user.setEmail("lea.meier@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(manager);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        //Check if dummy user exists
        User user4 = userRepository.findUserByEmailIgnoreCase("hans.zimmer@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Hans");
                    user.setLastName("Zimmer");
                    user.setEmail("hans.zimmer@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        //Check if dummy user exists
        User user5 = userRepository.findUserByEmailIgnoreCase("johann.fuchs@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Johann");
                    user.setLastName("Fuchs");
                    user.setEmail("johann.fuchs@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        //Check if dummy user exists
        User user6 = userRepository.findUserByEmailIgnoreCase("michael.pauli@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Michael");
                    user.setLastName("Pauli");
                    user.setEmail("michael.pauli@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        //Check if dummy user exists
        User user7 = userRepository.findUserByEmailIgnoreCase("juergen.zimmerer@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Jürgen");
                    user.setLastName("Zimmerer");
                    user.setEmail("juergen.zimmerer@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        User user8 = userRepository.findUserByEmailIgnoreCase("thomas.fuchs@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Thomas");
                    user.setLastName("Fuchs");
                    user.setEmail("thomas.fuchs@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });



        Company company_02 = new Company("bc", new Address("bc", "bc", "223", "bc"));
        companyRepository.save(company_02);
        User user9 = userRepository.findUserByEmailIgnoreCase("anna.beier@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Anna");
                    user.setLastName("Beier");
                    user.setEmail("anna.beier@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(employee);
                    user.setCompany(company_02);
                    return userRepository.save(user);
                });
        //Check if dummy user exists
        User user10 = userRepository.findUserByEmailIgnoreCase("bernd.becker@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Bernd");
                    user.setLastName("Becker");
                    user.setEmail("bernd.becker@abc.com");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole(owner);
                    user.setCompany(company_02);
                    return userRepository.save(user);
                });
        
        Client client1 = new Client();
        client1.setName("Alice");
        client1.setEmail("alice@abc.de");
        client1.setPhone("123");
        client1.setCompany(company);
        client1.setCreatedAt(LocalDateTime.now());
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Bob");
        client2.setEmail("bob@def.de");
        client2.setPhone("456");
        client2.setCompany(differentCompany);
        client2.setCreatedAt(LocalDateTime.now());
        clientRepository.save(client2);
    }
}
