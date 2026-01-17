package de.othr.hwwa.startup;

import de.othr.hwwa.model.*;
import de.othr.hwwa.repository.AuthorityRepositoryI;
import de.othr.hwwa.repository.ClientRepositoryI;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.RoleRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.repository.TaskRepositoryI;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.repository.MaterialRepositoryI;
import de.othr.hwwa.repository.TodoRepositoryI;
import de.othr.hwwa.repository.CommentRepositoryI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private final TodoRepositoryI todoRepository;
    private final CommentRepositoryI commentRepository;

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
            TodoRepositoryI todoRepository,
            CommentRepositoryI commentRepository
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
        this.todoRepository = todoRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) {
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

        Company company = new Company(
                "Schreinerei Sonnenschein",
                new Address("Sonnenweg 12", "München", "80331", "Deutschland")
        );
        companyRepository.save(company);

        Company company01 = new Company(
                "Elektro Beier GmbH",
                new Address("Industriestraße 5", "München", "80995", "Deutschland")
        );
        companyRepository.save(company01);

        Company company_02 = new Company(
                "Malerbetrieb Farbklecks",
                new Address("Hauptstraße 7", "Augsburg", "86150", "Deutschland")
        );
        companyRepository.save(company_02);

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

        ensureUserMeta(user1, 75.0f, "089 1111111");
        ensureUserMeta(user2, 48.0f, "089 2222222");
        ensureUserMeta(user3, 58.0f, "089 3333333");
        ensureUserMeta(user4, 46.0f, "089 4444444");
        ensureUserMeta(user5, 45.0f, "089 5555555");
        ensureUserMeta(user6, 47.0f, "089 6666666");
        ensureUserMeta(user7, 44.0f, "089 7777777");
        ensureUserMeta(user8, 46.5f, "089 8888888");
        ensureUserMeta(user9, 42.0f, "0821 111111");
        ensureUserMeta(user10, 65.0f, "0821 222222");

        LocalDateTime now = LocalDateTime.now();

        Client client1 = new Client();
        client1.setName("Familie Schneider – Altbauwohnung");
        client1.setEmail("schneider@example.de");
        client1.setPhone("089 9012345");
        client1.setCompany(company);
        client1.setCreatedAt(now.minusDays(10));
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Musterbau GmbH – Neubau EFH");
        client2.setEmail("kontakt@musterbau.de");
        client2.setPhone("089 9090909");
        client2.setCompany(company);
        client2.setCreatedAt(now.minusDays(5));
        clientRepository.save(client2);

        Client client3 = new Client();
        client3.setName("Elektro König KG – Büroausbau");
        client3.setEmail("info@elektro-koenig.de");
        client3.setPhone("0821 303030");
        client3.setCompany(company_02);
        client3.setCreatedAt(now.minusDays(7));
        clientRepository.save(client3);

        seedSchreinereiData(
                user1,
                user3,
                List.of(user2, user4, user5, user6, user7, user8),
                client1,
                client2
        );

        seedMalerbetriebData(
                user10,
                List.of(user9),
                client3
        );
    }

    private void ensureUserMeta(User user, float hourlyRate, String phoneNumber) {
        boolean changed = false;
        if (user.getHourlyRate() == 0f) {
            user.setHourlyRate(hourlyRate);
            changed = true;
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(phoneNumber);
            changed = true;
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now().minusDays(30));
            changed = true;
        }
        if (!user.isActive()) {
            user.setActive(true);
            changed = true;
        }
        if (changed) {
            userRepository.save(user);
        }
    }

    private void seedSchreinereiData(
            User owner,
            User manager,
            List<User> employees,
            Client clientAltbau,
            Client clientNeubau
    ) {
        LocalDateTime now = LocalDateTime.now();

        Task angebotKueche = createTaskIfMissing(
                "Angebot Einbauküche Schneider",
                "Einbauküche für Altbauwohnung planen inkl. Demontage, Montage und Entsorgung der alten Küche.",
                TaskStatus.PLANNED,
                now.minusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0),
                now.minusDays(2).withHour(11).withMinute(30).withSecond(0).withNano(0),
                owner,
                clientAltbau
        );

        Task aufmassSchlafzimmer = createTaskIfMissing(
                "Aufmaß Einbauschrank Schlafzimmer",
                "Vor-Ort-Aufmaß für maßgefertigten Einbauschrank im Schlafzimmer inkl. Skizze und Materialvorschlag.",
                TaskStatus.IN_PROGRESS,
                now.minusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0),
                now.minusDays(1).withHour(16).withMinute(0).withSecond(0).withNano(0),
                manager,
                clientAltbau
        );

        Task montageTreppenstufen = createTaskIfMissing(
                "Montage neue Treppenstufen",
                "Alte Treppenstufen demontieren und neue Massivholzstufen montieren, Oberflächen schleifen und ölen.",
                TaskStatus.IN_PROGRESS,
                now.plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(1).withHour(15).withMinute(0).withSecond(0).withNano(0),
                owner,
                clientNeubau
        );

        Task reparaturTuer = createTaskIfMissing(
                "Innentüren nachjustieren",
                "Mehrere klemmende Innentüren im Neubau nachjustieren und Beschläge prüfen.",
                TaskStatus.DONE,
                now.minusDays(3).withHour(9).withMinute(0).withSecond(0).withNano(0),
                now.minusDays(3).withHour(12).withMinute(0).withSecond(0).withNano(0),
                manager,
                clientNeubau
        );

        Task materialBestellen = createTaskIfMissing(
                "Material für Treppe bestellen",
                "Materialliste für Treppenstufen, Handlauf und Befestigungsmaterial prüfen und bestellen.",
                TaskStatus.PLANNED,
                now.plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(2).withHour(11).withMinute(0).withSecond(0).withNano(0),
                owner,
                clientNeubau
        );

        assignIfMissing(owner, angebotKueche, 60);
        assignIfMissing(manager, angebotKueche, 45);
        assignIfMissing(employees.get(0), aufmassSchlafzimmer, 90);
        assignIfMissing(owner, montageTreppenstufen, 30);
        assignIfMissing(employees.get(1), montageTreppenstufen, 180);
        assignIfMissing(employees.get(2), reparaturTuer, 120);
        assignIfMissing(manager, materialBestellen, 30);

        createTodoIfMissing(
                angebotKueche,
                owner,
                "Bestandsküche fotografieren",
                "Fotos von allen Seiten der bestehenden Küche für die Planung erstellen.",
                true
        );
        createTodoIfMissing(
                angebotKueche,
                owner,
                "Elektro- und Wasseranschlüsse klären",
                "Positionen der Anschlüsse mit Kunde und Elektriker abstimmen.",
                false
        );
        createTodoIfMissing(
                aufmassSchlafzimmer,
                manager,
                "Nischen exakt vermessen",
                "Höhe, Breite und Tiefe an mehreren Punkten messen und dokumentieren.",
                true
        );
        createTodoIfMissing(
                montageTreppenstufen,
                employees.get(1),
                "Alte Stufen demontieren",
                "Demontage ohne Beschädigung der Wangen, Entsorgung organisieren.",
                false
        );
        createTodoIfMissing(
                reparaturTuer,
                employees.get(2),
                "Beschläge schmieren",
                "Alle Bänder und Schlösser überprüfen und schmieren.",
                true
        );
        createTodoIfMissing(
                materialBestellen,
                manager,
                "Treppenstufen beim Lieferanten anfragen",
                "Preis und Lieferzeit für 14 Stufen Eiche massiv anfragen.",
                false
        );

        createMaterialIfMissing(
                montageTreppenstufen,
                "Massivholz-Treppenstufe Eiche",
                new BigDecimal("14.000"),
                new BigDecimal("95.00")
        );
        createMaterialIfMissing(
                montageTreppenstufen,
                "Handlauf Eiche geölt",
                new BigDecimal("7.500"),
                new BigDecimal("45.00")
        );
        createMaterialIfMissing(
                montageTreppenstufen,
                "Schrauben und Dübel Set",
                new BigDecimal("1.000"),
                new BigDecimal("35.00")
        );
        createMaterialIfMissing(
                angebotKueche,
                "Korpusse Spanplatte weiß",
                new BigDecimal("6.000"),
                new BigDecimal("120.00")
        );
        createMaterialIfMissing(
                materialBestellen,
                "Arbeitsplatte Eiche geölt",
                new BigDecimal("3.600"),
                new BigDecimal("210.00")
        );

        createCommentIfMissing(
                angebotKueche,
                owner,
                "Kunde wünscht zusätzlich LED‑Beleuchtung unter den Oberschränken.",
                now.minusDays(1).withHour(16).withMinute(30)
        );
        createCommentIfMissing(
                angebotKueche,
                manager,
                "Statik der Altbauwand prüfen, ob Hängeschränke voll belastbar montiert werden können.",
                now.minusHours(5)
        );
        createCommentIfMissing(
                montageTreppenstufen,
                employees.get(1),
                "Treppenhaus ist eng, Kleinteile vorab in Werkstatt vormontieren.",
                now.plusDays(1).withHour(7).withMinute(30)
        );
        createCommentIfMissing(
                reparaturTuer,
                employees.get(2),
                "Alle Türen laufen wieder sauber, Kunde sehr zufrieden.",
                now.minusDays(2).withHour(13).withMinute(15)
        );
    }

    private void seedMalerbetriebData(
            User owner,
            List<User> employees,
            Client clientBueros
    ) {
        LocalDateTime now = LocalDateTime.now();

        Task bueroStreichen = createTaskIfMissing(
                "Büroräume neu streichen",
                "Büroräume im 2. OG in weiß und Akzentwand in Firmenfarbe streichen, inkl. Abdecken und Endreinigung.",
                TaskStatus.IN_PROGRESS,
                now.plusDays(3).withHour(8).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(3).withHour(17).withMinute(0).withSecond(0).withNano(0),
                owner,
                clientBueros
        );

        Task lackArbeiten = createTaskIfMissing(
                "Türen und Zargen lackieren",
                "Bestehende Türen anschleifen und mit Lack im RAL‑Ton des Corporate Designs lackieren.",
                TaskStatus.PLANNED,
                now.plusDays(4).withHour(9).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(4).withHour(15).withMinute(0).withSecond(0).withNano(0),
                owner,
                clientBueros
        );

        assignIfMissing(owner, bueroStreichen, 45);
        assignIfMissing(employees.get(0), bueroStreichen, 120);
        assignIfMissing(employees.get(0), lackArbeiten, 30);

        createTodoIfMissing(
                bueroStreichen,
                owner,
                "Möbel abdecken und abkleben",
                "Alle Tische, Schränke und Sockelleisten sorgfältig abdecken.",
                false
        );
        createTodoIfMissing(
                bueroStreichen,
                employees.get(0),
                "Zweite Schicht prüfen",
                "Deckkraft nach erster Schicht kontrollieren, ggf. zweite Schicht einplanen.",
                false
        );
        createTodoIfMissing(
                lackArbeiten,
                employees.get(0),
                "Schleifarbeiten vorbereiten",
                "Türen aushängen und auf Böcke legen, Schleifpapier vorbereiten.",
                false
        );

        createMaterialIfMissing(
                bueroStreichen,
                "Dispersionsfarbe weiß, 12l",
                new BigDecimal("3.000"),
                new BigDecimal("65.00")
        );
        createMaterialIfMissing(
                bueroStreichen,
                "Abdeckfolie und Malerkrepp",
                new BigDecimal("1.000"),
                new BigDecimal("25.00")
        );
        createMaterialIfMissing(
                lackArbeiten,
                "Lack seidenmatt, RAL‑Farbe",
                new BigDecimal("2.000"),
                new BigDecimal("80.00")
        );

        createCommentIfMissing(
                bueroStreichen,
                owner,
                "Kunde wünscht, dass mindestens ein Besprechungsraum jederzeit nutzbar bleibt – Etappeneinteilung beachten.",
                now.plusDays(2).withHour(10).withMinute(0)
        );
        createCommentIfMissing(
                lackArbeiten,
                employees.get(0),
                "Bitte Staubschutz besonders beachten, viele IT‑Geräte im Flur.",
                now.plusDays(3).withHour(15).withMinute(45)
        );
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
                t.setDescription(description);
                t.setStatus(status);
                t.setStartDateTime(start);
                t.setEndDateTime(end);
                t.setCreatedBy(createdBy);
                t.setClient(client);
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

    private Todo createTodoIfMissing(Task task,
                                     User createdBy,
                                     String title,
                                     String description,
                                     boolean done) {
        List<Todo> existing = todoRepository.findByTaskIdOrderByDoneAscIdAsc(task.getId());
        for (Todo t : existing) {
            if (t.getTitle() != null && t.getTitle().equalsIgnoreCase(title)) {
                t.setDescription(description);
                t.setDone(done);
                if (t.getCreatedByUser() == null) {
                    t.setCreatedByUser(createdBy);
                }
                return todoRepository.save(t);
            }
        }

        Todo todo = new Todo();
        todo.setTask(task);
        todo.setCreatedByUser(createdBy);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDone(done);

        return todoRepository.save(todo);
    }

    private Material createMaterialIfMissing(Task task,
                                             String name,
                                             BigDecimal quantity,
                                             BigDecimal unitPrice) {
        List<Material> existing = materialRepository.findByTaskIdOrderByIdAsc(task.getId());
        for (Material m : existing) {
            if (m.getName() != null && m.getName().equalsIgnoreCase(name)) {
                m.setQuantity(quantity);
                m.setUnitPrice(unitPrice);
                return materialRepository.save(m);
            }
        }

        Material material = new Material();
        material.setTask(task);
        material.setName(name);
        material.setQuantity(quantity);
        material.setUnitPrice(unitPrice);

        return materialRepository.save(material);
    }

    private Comment createCommentIfMissing(Task task,
                                           User createdBy,
                                           String text,
                                           LocalDateTime createdAt) {
        List<Comment> existing = commentRepository.findByTaskIdOrderByCreatedAtDesc(task.getId());
        for (Comment c : existing) {
            if (c.getText() != null && c.getText().equalsIgnoreCase(text)) {
                if (c.getCreatedByUser() == null) {
                    c.setCreatedByUser(createdBy);
                }
                if (c.getCreatedAt() == null) {
                    c.setCreatedAt(createdAt);
                }
                return commentRepository.save(c);
            }
        }

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setCreatedByUser(createdBy);
        comment.setText(text);
        comment.setCreatedAt(createdAt);

        return commentRepository.save(comment);
    }
}