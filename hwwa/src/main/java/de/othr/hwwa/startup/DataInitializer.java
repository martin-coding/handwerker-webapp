package de.othr.hwwa.startup;

import de.othr.hwwa.model.*;
import de.othr.hwwa.model.jwt.ApiUser;
import de.othr.hwwa.repository.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepositoryI userRepository;
    private final ApiUserRepositoryI apiUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataInitializerHelper helper;

    public DataInitializer(
            UserRepositoryI userRepository,
            ApiUserRepositoryI apiUserRepository,
            PasswordEncoder passwordEncoder,
            DataInitializerHelper helper
    ) {
        this.userRepository = userRepository;
        this.apiUserRepository = apiUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.helper = helper;
    }

    @Override
    public void run(String... args) {
        //Create authorities
        Authority tasks = helper.createAuthority("tasks");
        Authority basic = helper.createAuthority("basic");
        Authority createUser = helper.createAuthority("createUser");
        Authority manageEmployees = helper.createAuthority("manageEmployees");
        Authority updateCompanyData = helper.createAuthority("updateCompanyData");
        Authority manageClients = helper.createAuthority("manageClients");
        Authority manageInvoices = helper.createAuthority("manageInvoices");
        Authority manageDashboard = helper.createAuthority("manageDashboard");

        Role employee = helper.createRole("Employee", Set.of(tasks, basic));
        Role manager = helper.createRole("Manager", Set.of(tasks, basic, manageClients, manageDashboard, manageInvoices));
        Role owner = helper.createRole("Owner", Set.of(tasks, createUser, manageEmployees, basic, updateCompanyData, manageClients, manageInvoices, manageDashboard));

        Company company = helper.createCompany("Schreinerei Sonnenschein", new Address("Sonnenweg 12", "München", "80331", "Deutschland"));
        Company company1 = helper.createCompany("Elektro Beier GmbH", new Address("Industriestraße 5", "München", "80995", "Deutschland"));
        Company company2 = helper.createCompany("Malerbetrieb Farbklecks", new Address("Hauptstraße 7", "Augsburg", "86150", "Deutschland"));

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
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setCompany(company2);
                    user.setCreatedAt(LocalDateTime.now());
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
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
                    user.setCompany(company2);
                    user.setCreatedAt(LocalDateTime.now());
                    user.setPhoneNumber("0821 222222");
                    user.setHourlyRate(65.0f);
                    return userRepository.save(user);
                });

        ApiUser apiUser_01 = apiUserRepository.findById((long) 1)
                .orElseGet(()->{
                    ApiUser apiUser = new ApiUser();
                    apiUser.setEmail("max.meier@abc.com");
                    apiUser.setPassword(passwordEncoder.encode("12345678"));
                    apiUser.setCompanyId(user1.getCompany().getId());
                    return apiUserRepository.save(apiUser);
                });

        Client client1 = helper.createClient(company, "Schneider Hans", "schneider@example.de", "089 9012345",new Address("32 Galgenbergstraße", "Regensburg", "93053", "Deutschland"), LocalDateTime.now().minusDays(10));
        Client client2 = helper.createClient(company, "Musterbau GmbH – Neubau EFH", "muster@firma.de", "089 9090909",new Address("32 Galgenbergstraße", "Regensburg", "93053", "Deutschland"), LocalDateTime.now().minusDays(5));
        Client client3 = helper.createClient(company2, "Elektro König KG – Büroausbau", "info@elektro-koenig.de", "0821 303030",new Address("32 Galgenbergstraße", "Regensburg", "93053", "Deutschland"), LocalDateTime.now().minusDays(7));

        seedSchreinereiData(
                user1,
                user3,
                List.of(user2, user4, user5, user6, user7, user8),
                client1,
                client2,
                company
        );

        seedMalerbetriebData(
                user10,
                List.of(user9),
                client3,
                company2
        );
    }

    private void seedSchreinereiData(
            User owner,
            User manager,
            List<User> employees,
            Client clientAltbau,
            Client clientNeubau,
            Company company
    ) {
        LocalDateTime now = LocalDateTime.now();

        Task angebotKueche = helper.createTaskIfMissing(
                "Angebot Einbauküche Schneider",
                "Einbauküche für Altbauwohnung planen inkl. Demontage, Montage und Entsorgung der alten Küche.",
                TaskStatus.PLANNED,
                now.minusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0),
                now.minusDays(2).withHour(11).withMinute(30).withSecond(0).withNano(0),
                owner,
                clientAltbau,
                company
        );

        Task aufmassSchlafzimmer = helper.createTaskIfMissing(
                "Aufmaß Einbauschrank Schlafzimmer",
                "Vor-Ort-Aufmaß für maßgefertigten Einbauschrank im Schlafzimmer inkl. Skizze und Materialvorschlag.",
                TaskStatus.IN_PROGRESS,
                now.minusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0),
                now.minusDays(1).withHour(16).withMinute(0).withSecond(0).withNano(0),
                manager,
                clientAltbau,
                company
        );

        Task montageTreppenstufen = helper.createTaskIfMissing(
                "Montage neue Treppenstufen",
                "Alte Treppenstufen demontieren und neue Massivholzstufen montieren, Oberflächen schleifen und ölen.",
                TaskStatus.IN_PROGRESS,
                now.plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(1).withHour(15).withMinute(0).withSecond(0).withNano(0),
                owner,
                clientNeubau,
                company
        );

        Task reparaturTuer = helper.createTaskIfMissing(
                "Innentüren nachjustieren",
                "Mehrere klemmende Innentüren im Neubau nachjustieren und Beschläge prüfen.",
                TaskStatus.DONE,
                now.minusDays(3).withHour(9).withMinute(0).withSecond(0).withNano(0),
                now.minusDays(3).withHour(12).withMinute(0).withSecond(0).withNano(0),
                manager,
                clientNeubau,
                company
        );

        Task materialBestellen = helper.createTaskIfMissing(
                "Material für Treppe bestellen",
                "Materialliste für Treppenstufen, Handlauf und Befestigungsmaterial prüfen und bestellen.",
                TaskStatus.PLANNED,
                now.plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(2).withHour(11).withMinute(0).withSecond(0).withNano(0),
                owner,
                clientNeubau,
                company
        );

        helper.assignIfMissing(owner, angebotKueche, 60);
        helper.assignIfMissing(manager, angebotKueche, 45);
        helper.assignIfMissing(employees.get(0), aufmassSchlafzimmer, 90);
        helper.assignIfMissing(owner, montageTreppenstufen, 30);
        helper.assignIfMissing(employees.get(1), montageTreppenstufen, 180);
        helper.assignIfMissing(employees.get(2), reparaturTuer, 120);
        helper.assignIfMissing(manager, materialBestellen, 30);

        helper.createTodoIfMissing(
                angebotKueche,
                owner,
                "Bestandsküche fotografieren",
                "Fotos von allen Seiten der bestehenden Küche für die Planung erstellen.",
                true
        );
        helper.createTodoIfMissing(
                angebotKueche,
                owner,
                "Elektro- und Wasseranschlüsse klären",
                "Positionen der Anschlüsse mit Kunde und Elektriker abstimmen.",
                false
        );
        helper.createTodoIfMissing(
                aufmassSchlafzimmer,
                manager,
                "Nischen exakt vermessen",
                "Höhe, Breite und Tiefe an mehreren Punkten messen und dokumentieren.",
                true
        );
        helper.createTodoIfMissing(
                montageTreppenstufen,
                employees.get(1),
                "Alte Stufen demontieren",
                "Demontage ohne Beschädigung der Wangen, Entsorgung organisieren.",
                false
        );
        helper.createTodoIfMissing(
                reparaturTuer,
                employees.get(2),
                "Beschläge schmieren",
                "Alle Bänder und Schlösser überprüfen und schmieren.",
                true
        );
        helper.createTodoIfMissing(
                materialBestellen,
                manager,
                "Treppenstufen beim Lieferanten anfragen",
                "Preis und Lieferzeit für 14 Stufen Eiche massiv anfragen.",
                false
        );

        helper.createMaterialIfMissing(
                montageTreppenstufen,
                "Massivholz-Treppenstufe Eiche",
                14,
                95
        );
        helper.createMaterialIfMissing(
                montageTreppenstufen,
                "Handlauf Eiche geölt",
                7.5F,
                45
        );
        helper.createMaterialIfMissing(
                montageTreppenstufen,
                "Schrauben und Dübel Set",
                1,
                35
        );
        helper.createMaterialIfMissing(
                angebotKueche,
                "Korpusse Spanplatte weiß",
                6,
                120
        );
        helper.createMaterialIfMissing(
                materialBestellen,
                "Arbeitsplatte Eiche geölt",
                3.6F,
                210
        );

        helper.createCommentIfMissing(
                angebotKueche,
                owner,
                "Kunde wünscht zusätzlich LED‑Beleuchtung unter den Oberschränken.",
                now.minusDays(1).withHour(16).withMinute(30)
        );
        helper.createCommentIfMissing(
                angebotKueche,
                manager,
                "Statik der Altbauwand prüfen, ob Hängeschränke voll belastbar montiert werden können.",
                now.minusHours(5)
        );
        helper.createCommentIfMissing(
                montageTreppenstufen,
                employees.get(1),
                "Treppenhaus ist eng, Kleinteile vorab in Werkstatt vormontieren.",
                now.plusDays(1).withHour(7).withMinute(30)
        );
        helper.createCommentIfMissing(
                reparaturTuer,
                employees.get(2),
                "Alle Türen laufen wieder sauber, Kunde sehr zufrieden.",
                now.minusDays(2).withHour(13).withMinute(15)
        );
    }

    private void seedMalerbetriebData(
            User owner,
            List<User> employees,
            Client clientBueros,
            Company company
    ) {
        LocalDateTime now = LocalDateTime.now();

        Task bueroStreichen = helper.createTaskIfMissing(
                "Büroräume neu streichen",
                "Büroräume im 2. OG in weiß und Akzentwand in Firmenfarbe streichen, inkl. Abdecken und Endreinigung.",
                TaskStatus.IN_PROGRESS,
                now.plusDays(3).withHour(8).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(3).withHour(17).withMinute(0).withSecond(0).withNano(0),
                owner,
                clientBueros,
                company
        );

        Task lackArbeiten = helper.createTaskIfMissing(
                "Türen und Zargen lackieren",
                "Bestehende Türen anschleifen und mit Lack im RAL‑Ton des Corporate Designs lackieren.",
                TaskStatus.PLANNED,
                now.plusDays(4).withHour(9).withMinute(0).withSecond(0).withNano(0),
                now.plusDays(4).withHour(15).withMinute(0).withSecond(0).withNano(0),
                owner,
                clientBueros,
                company
        );

        helper.assignIfMissing(owner, bueroStreichen, 45);
        helper.assignIfMissing(employees.get(0), bueroStreichen, 120);
        helper.assignIfMissing(employees.get(0), lackArbeiten, 30);

        helper.createTodoIfMissing(
                bueroStreichen,
                owner,
                "Möbel abdecken und abkleben",
                "Alle Tische, Schränke und Sockelleisten sorgfältig abdecken.",
                false
        );
        helper.createTodoIfMissing(
                bueroStreichen,
                employees.get(0),
                "Zweite Schicht prüfen",
                "Deckkraft nach erster Schicht kontrollieren, ggf. zweite Schicht einplanen.",
                false
        );
        helper.createTodoIfMissing(
                lackArbeiten,
                employees.get(0),
                "Schleifarbeiten vorbereiten",
                "Türen aushängen und auf Böcke legen, Schleifpapier vorbereiten.",
                false
        );

        helper.createMaterialIfMissing(
                bueroStreichen,
                "Dispersionsfarbe weiß, 12l",
                3,
                65
        );
        helper.createMaterialIfMissing(
                bueroStreichen,
                "Abdeckfolie und Malerkrepp",
                1,
                25
        );
        helper.createMaterialIfMissing(
                lackArbeiten,
                "Lack seidenmatt, RAL‑Farbe",
                2,
                80
        );

        helper.createCommentIfMissing(
                bueroStreichen,
                owner,
                "Kunde wünscht, dass mindestens ein Besprechungsraum jederzeit nutzbar bleibt – Etappeneinteilung beachten.",
                now.plusDays(2).withHour(10).withMinute(0)
        );
        helper.createCommentIfMissing(
                lackArbeiten,
                employees.get(0),
                "Bitte Staubschutz besonders beachten, viele IT‑Geräte im Flur.",
                now.plusDays(3).withHour(15).withMinute(45)
        );
    }
}