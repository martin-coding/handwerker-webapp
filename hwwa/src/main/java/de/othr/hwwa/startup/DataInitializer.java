package de.othr.hwwa.startup;

import de.othr.hwwa.model.*;
import de.othr.hwwa.repository.AuthorityRepositoryI;
import de.othr.hwwa.repository.ClientRepositoryI;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.RoleRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.repository.TaskRepositoryI;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryI roleRepository;
    private final AuthorityRepositoryI authorityRepository;
    private final UserRepositoryI userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepositoryI companyRepository;
    private final ClientRepositoryI clientRepository;
    private final TaskRepositoryI taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;

    public DataInitializer(
            RoleRepositoryI roleRepository,
            AuthorityRepositoryI authorityRepository,
            UserRepositoryI userRepository,
            PasswordEncoder passwordEncoder,
            ClientRepositoryI clientRepository,
            CompanyRepositoryI companyRepository,
            TaskRepositoryI taskRepository,
            TaskAssignmentRepository taskAssignmentRepository
    ) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    @Override
    public void run(String... args) {
        // Check if authorities exist
        Authority tasks = authorityRepository.findByName("tasks")
                .orElseGet(() -> authorityRepository.save(new Authority("tasks")));
        Authority basic = authorityRepository.findByName("basic")
                .orElseGet(() -> authorityRepository.save(new Authority("basic")));
        Authority createUser = authorityRepository.findByName("createUser")
                .orElseGet(() -> authorityRepository.save(new Authority("createUser")));
        Authority manageEmployees = authorityRepository.findByName("manageEmployees")
                .orElseGet(() -> authorityRepository.save(new Authority("manageEmployees")));
        Authority updateCompanyData = authorityRepository.findByName("updateCompanyData")
                .orElseGet(() -> authorityRepository.save(new Authority("updateCompanyData")));
        Authority manageClients = authorityRepository.findByName("manageClients")
                .orElseGet(() -> authorityRepository.save(new Authority("manageClients")));

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
                    r.setAuthorities(Set.of(tasks, createUser, manageEmployees, basic, updateCompanyData, manageClients));
                    return roleRepository.save(r);
                });


        Company company = new Company("Schreiner Test", new Address("Sonnenweg", "München", "94921", "Deutschland"));
        companyRepository.save(company);
        Company company01 = new Company("def", new Address("def", "def", "456", "def"));
        companyRepository.save(company01);

        //Check if dummy user exists
        User user1 = userRepository.findUserByEmailIgnoreCase("thomas.test@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Thomas");
                    user.setLastName("Test");
                    user.setEmail("thomas.test@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(owner);
                    user.setCompany(company);
                    return userRepository.save(user);
                });

        User user2 = userRepository.findUserByEmailIgnoreCase("sarah.mueller@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Sarah");
                    user.setLastName("Mueller");
                    user.setEmail("sarah.mueller@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        User user3 = userRepository.findUserByEmailIgnoreCase("lea.meier@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Lea");
                    user.setLastName("Meier");
                    user.setEmail("lea.meier@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(manager);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        User user4 = userRepository.findUserByEmailIgnoreCase("hans.zimmer@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Hans");
                    user.setLastName("Zimmer");
                    user.setEmail("hans.zimmer@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        User user5 = userRepository.findUserByEmailIgnoreCase("johann.fuchs@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Johann");
                    user.setLastName("Fuchs");
                    user.setEmail("johann.fuchs@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        User user6 = userRepository.findUserByEmailIgnoreCase("michael.pauli@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Michael");
                    user.setLastName("Pauli");
                    user.setEmail("michael.pauli@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(employee);
                    user.setCompany(company);
                    return userRepository.save(user);
                });
        User user7 = userRepository.findUserByEmailIgnoreCase("juergen.zimmerer@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Jürgen");
                    user.setLastName("Zimmerer");
                    user.setEmail("juergen.zimmerer@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
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
                    user.setPassword(passwordEncoder.encode("12345678"));
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
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(employee);
                    user.setCompany(company_02);
                    return userRepository.save(user);
                });
        User user10 = userRepository.findUserByEmailIgnoreCase("bernd.becker@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Bernd");
                    user.setLastName("Becker");
                    user.setEmail("bernd.becker@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
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
        client2.setCompany(company01);
        client2.setCreatedAt(LocalDateTime.now());
        clientRepository.save(client2);

        seedTasks(user1, client1);
    }

    private void seedTasks(User user, Client client) {
        LocalDateTime now = LocalDateTime.now();

        Task t1 = createTaskIfMissing(
                "Angebot erstellen",
                "Angebot für Kunde Muster GmbH vorbereiten und kalkulieren.",
                TaskStatus.IN_PROGRESS,
                now.minusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0),
                now.minusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0),
                user,
                client
        );

        Task t2 = createTaskIfMissing(
                "Material bestellen",
                "Materialliste prüfen und fehlende Positionen bestellen.",
                TaskStatus.PLANNED,
                now.plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(1).withHour(11).withMinute(30).withSecond(0).withNano(0),
                user,
                client
        );

        Task t3 = createTaskIfMissing(
                "Baustelle prüfen",
                "Vor-Ort Termin zur Einschätzung, Fotos machen, Risiken notieren.",
                TaskStatus.DONE,
                now.minusDays(3).withHour(14).withMinute(0).withSecond(0).withNano(0),
                now.minusDays(3).withHour(16).withMinute(0).withSecond(0).withNano(0),
                user,
                client
        );

        assignIfMissing(user, t1, 90);
        assignIfMissing(user, t2, 45);
        assignIfMissing(user, t3, 120);
    }

    private Task createTaskIfMissing(String title,
                                     String description,
                                     TaskStatus status,
                                     LocalDateTime start,
                                     LocalDateTime end,
                                     User createdBy,
                                     Client client) {

        List<Task> existing = taskRepository.findByTitleContainingIgnoreCase(title);
        for (Task t : existing) {
            if (t.getTitle() != null && t.getTitle().equalsIgnoreCase(title)) {

                if (t.getStatus() == null) t.setStatus(status);
                if (t.getStartDateTime() == null) t.setStartDateTime(start);
                if (t.getEndDateTime() == null) t.setEndDateTime(end);
                if (t.getCreatedBy() == null) t.setCreatedBy(createdBy);
                if (t.getClient() == null) t.setClient(client);

                return taskRepository.save(t);
            }
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setStartDateTime(start);
        task.setEndDateTime(end);
        task.setCreatedBy(createdBy);
        task.setClient(client);

        return taskRepository.save(task);
    }

    private void assignIfMissing(User user, Task task, int initialMinutes) {
        boolean exists = taskAssignmentRepository
                .findByUserIdAndTaskId(user.getId(), task.getId())
                .isPresent();

        if (!exists) {
            TaskAssignment assignment = new TaskAssignment(user, task);
            assignment.setMinutesWorked(initialMinutes);
            taskAssignmentRepository.save(assignment);
        }
    }
}