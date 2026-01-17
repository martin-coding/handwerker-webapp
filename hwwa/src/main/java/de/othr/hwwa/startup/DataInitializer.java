package de.othr.hwwa.startup;

import de.othr.hwwa.model.*;
import de.othr.hwwa.model.jwt.ApiUser;
import de.othr.hwwa.repository.*;
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
    private final MaterialRepositoryI materialRepository;
    private final ApiUserRepositoryI apiUserRepository;

    public DataInitializer(
            RoleRepositoryI roleRepository,
            AuthorityRepositoryI authorityRepository,
            UserRepositoryI userRepository,
            PasswordEncoder passwordEncoder,
            ClientRepositoryI clientRepository,
            CompanyRepositoryI companyRepository,
            TaskRepositoryI taskRepository,
            TaskAssignmentRepository taskAssignmentRepository,
            MaterialRepositoryI materialRepository,
            ApiUserRepositoryI apiUserRepository
    ) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.materialRepository = materialRepository;
        this.apiUserRepository = apiUserRepository;
    }

    @Override
    public void run(String... args) {
        //Create authorities
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
        Authority manageInvoices = authorityRepository.findByName("manageInvoices")
                .orElseGet(() -> authorityRepository.save(new Authority("manageInvoices")));

        //Create roles with authorities
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
                    r.setAuthorities(Set.of(tasks, createUser,manageEmployees, basic, updateCompanyData, manageClients, manageInvoices));
                    return roleRepository.save(r);
                });


        //Create companies
        Company company = new Company("Schreiner Test", new Address("Sonnenweg", "München", "94921", "Deutschland"));
        companyRepository.save(company);
        Company company01 = new Company("def", new Address("def", "def", "456", "def"));
        companyRepository.save(company01);
        Company company_02 = new Company("bc", new Address("bc", "bc", "223", "bc"));
        companyRepository.save(company_02);

        //Create users
        User user1 = userRepository.findUserByEmailIgnoreCase("thomas.test@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Thomas");
                    user.setLastName("Test");
                    user.setEmail("thomas.test@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(owner);
                    user.setCompany(company);
                    user.setCreatedAt(LocalDateTime.now());
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
                    user.setCreatedAt(LocalDateTime.now());
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
                    user.setCreatedAt(LocalDateTime.now());
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
                    user.setCreatedAt(LocalDateTime.now());
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
                    user.setCreatedAt(LocalDateTime.now());
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
                    user.setCreatedAt(LocalDateTime.now());
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
                    user.setCreatedAt(LocalDateTime.now());
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
                    user.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                });


        User user9 = userRepository.findUserByEmailIgnoreCase("anna.beier@abc.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Anna");
                    user.setLastName("Beier");
                    user.setEmail("anna.beier@abc.com");
                    user.setPassword(passwordEncoder.encode("12345678"));
                    user.setRole(employee);
                    user.setCompany(company_02);
                    user.setCreatedAt(LocalDateTime.now());
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
                    user.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                });


        //Create clients for company
        Client client1 = clientRepository.findById((long)1)
                .orElseGet(() -> {
                    Client client = new Client();
                    client.setName("Alexa Müller");
                    client.setEmail("jan.hoepfl@st.oth-regensburg.de");
                    client.setPhone("123");
                    client.setCompany(company);
                    client.setCreatedAt(LocalDateTime.now());
                    client.setAddress(new Address("Sonnenweg", "München", "94921", "Deutschland"));
                    return clientRepository.save(client);
                });

        Client client2 = clientRepository.findById((long)2)
                .orElseGet(() -> {
                    Client client = new Client();
                    client.setName("Bob Schäffer");
                    client.setEmail("bob@gmx.de");
                    client.setPhone("456");
                    client.setCompany(company);
                    client.setCreatedAt(LocalDateTime.now());
                    client.setAddress(new Address("Sonnenweg", "München", "94921", "Deutschland"));
                    return clientRepository.save(client);
                });

        Client client3 = clientRepository.findById((long)3)
                .orElseGet(() -> {
                    Client client = new Client();
                    client.setName("Tom Meier");
                    client.setEmail("tom@gmx.de");
                    client.setPhone("+49 034 55783");
                    client.setCompany(company01);
                    client.setCreatedAt(LocalDateTime.now());
                    client.setAddress(new Address("Sonnenweg", "München", "94921", "Deutschland"));
                    return clientRepository.save(client);
                });

        //Create tasks
        Task task1 = taskRepository.findById((long)1)
                .orElseGet(() -> {
                    Task task = new Task();
                    task.setTitle("Gartenarbeiten");
                    task.setDescription("Stützmauer muss aufgestellt werden");
                    task.setStartDateTime(LocalDateTime.now().minusDays(1).withHour(9));
                    task.setEndDateTime(LocalDateTime.now().minusDays(1).withHour(12));
                    task.setCreatedBy(user1);
                    task.setCompanyId(user1.getCompany().getId());
                    task.setClient(client1);
                    task.setStatus(TaskStatus.DONE);
                    return taskRepository.save(task);
                });

        Task task2 = taskRepository.findById((long)2)
                .orElseGet(() -> {
                    Task task = new Task();
                    task.setTitle("Pflastern");
                    task.setDescription("Pflaster muss vor Garage verlegt werden.");
                    task.setStartDateTime(LocalDateTime.now().minusDays(1).withHour(9));
                    task.setEndDateTime(LocalDateTime.now().minusDays(1).withHour(12));
                    task.setCreatedBy(user1);
                    task.setCompanyId(user1.getCompany().getId());
                    task.setClient(client1);
                    task.setStatus(TaskStatus.DONE);
                    return taskRepository.save(task);
                });

        Task task3 = taskRepository.findById((long)3)
                .orElseGet(() -> {
                    Task task = new Task();
                    task.setTitle("Schrank bauen");
                    task.setDescription("Holzschrank in Wohnzimmer aufbauen");
                    task.setStartDateTime(LocalDateTime.now().minusDays(1).withHour(9));
                    task.setEndDateTime(LocalDateTime.now().minusDays(1).withHour(12));
                    task.setCreatedBy(user1);
                    task.setCompanyId(user1.getCompany().getId());
                    task.setClient(client2);
                    task.setStatus(TaskStatus.DONE);
                    return taskRepository.save(task);
                });

        //Create materials
        Material material1 = materialRepository.findById((long) 1)
                .orElseGet(() -> {
                    Material material = new Material();
                    material.setDescription("1qm Rollrasen");
                    material.setCount(120);
                    material.setTask(task1);
                    return materialRepository.save(material);
                });

        Material material2 = materialRepository.findById((long) 2)
                .orElseGet(() -> {
                    Material material = new Material();
                    material.setDescription("1t Erde");
                    material.setCount(10);
                    material.setTask(task1);
                    return materialRepository.save(material);
                });

        //Assign users to tasks
        TaskAssignment assignment1 = taskAssignmentRepository.findByUserIdAndTaskId(user2.getId(), task1.getId())
                .orElseGet(() -> {
                    TaskAssignment taskAssignment = new TaskAssignment(user2, task1);
                    taskAssignment.setMinutesWorked(90);
                    return taskAssignmentRepository.save(taskAssignment);
                });

        TaskAssignment assignment2 = taskAssignmentRepository.findByUserIdAndTaskId(user3.getId(), task1.getId())
                .orElseGet(() -> {
                    TaskAssignment taskAssignment = new TaskAssignment(user2, task1);
                    taskAssignment.setMinutesWorked(90);
                    return taskAssignmentRepository.save(taskAssignment);
                });

        TaskAssignment assignment3 = taskAssignmentRepository.findByUserIdAndTaskId(user4.getId(), task1.getId())
                .orElseGet(() -> {
                    TaskAssignment taskAssignment = new TaskAssignment(user4, task1);
                    taskAssignment.setMinutesWorked(90);
                    return taskAssignmentRepository.save(taskAssignment);
                });

        TaskAssignment assignment4 = taskAssignmentRepository.findByUserIdAndTaskId(user2.getId(), task2.getId())
                .orElseGet(() -> {
                    TaskAssignment taskAssignment = new TaskAssignment(user2, task2);
                    taskAssignment.setMinutesWorked(90);
                    return taskAssignmentRepository.save(taskAssignment);
                });

        TaskAssignment assignment5 = taskAssignmentRepository.findByUserIdAndTaskId(user2.getId(), task3.getId())
                .orElseGet(() -> {
                    TaskAssignment taskAssignment = new TaskAssignment(user2, task3);
                    taskAssignment.setMinutesWorked(90);
                    return taskAssignmentRepository.save(taskAssignment);
                });


        //Create account for apiUser
        ApiUser apiUser_01 = apiUserRepository.findById((long) 1)
                .orElseGet(()->{
                    ApiUser apiUser = new ApiUser();
                    apiUser.setEmail("max.meier@abc.com");
                    apiUser.setPassword(passwordEncoder.encode("12345678"));
                    apiUser.setCompanyId(user1.getCompany().getId());
                    return apiUserRepository.save(apiUser);
        });
    }
}