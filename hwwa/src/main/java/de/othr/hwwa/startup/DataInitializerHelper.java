package de.othr.hwwa.startup;

import de.othr.hwwa.model.*;
import de.othr.hwwa.repository.*;
import de.othr.hwwa.model.*;
import de.othr.hwwa.repository.*;
import de.othr.hwwa.repository.AuthorityRepositoryI;
import de.othr.hwwa.repository.ClientRepositoryI;
import de.othr.hwwa.repository.CommentRepositoryI;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.MaterialRepositoryI;
import de.othr.hwwa.repository.RoleRepositoryI;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.repository.TaskRepositoryI;
import de.othr.hwwa.repository.TodoRepositoryI;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializerHelper {

    private final RoleRepositoryI roleRepository;
    private final AuthorityRepositoryI authorityRepository;
    private final CompanyRepositoryI companyRepository;
    private final ClientRepositoryI clientRepository;
    private final TaskRepositoryI taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final MaterialRepositoryI materialRepository;
    private final TodoRepositoryI todoRepository;
    private final CommentRepositoryI commentRepository;


    public DataInitializerHelper(
            RoleRepositoryI roleRepository,
            AuthorityRepositoryI authorityRepository,
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
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.materialRepository = materialRepository;
        this.todoRepository = todoRepository;
        this.commentRepository = commentRepository;
    }


    public Authority createAuthority(String name) {
        return authorityRepository.findByName(name)
                .orElseGet(() -> authorityRepository.save(new Authority(name)));
    }

    public Role createRole(String name, Set<Authority> authorities) {
        return roleRepository.findByName(name)
                .map(existing -> {
                    existing.setName(name);
                    existing.setAuthorities(authorities);
                    return roleRepository.save(existing);
                })
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(name);
                    r.setAuthorities(authorities);
                    return roleRepository.save(r);
                });
    }

    public Company createCompany(String name, Address address) {
        Company existing = companyRepository.findAll().stream()
                .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(name))
                .filter(c -> c.getAddress() != null && c.getAddress().equals(address))
                .findFirst()
                .orElse(null);

        if (existing == null) {
            return companyRepository.save(new Company(name, address));
        }

        existing.setName(name);
        existing.setAddress(address);
        return companyRepository.save(existing);
    }

    public Client createClient(Company company,
                                String name,
                                String email,
                                String phone,
                                Address address,
                                LocalDateTime createdAt) {

        Client existing = clientRepository.findByCompanyIdAndActiveTrueOrderByNameAsc(company.getId()).stream()
                .filter(c -> c.getEmail() != null && c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (existing == null) {
            Client c = new Client();
            c.setName(name);
            c.setEmail(email);
            c.setPhone(phone);
            c.setCompany(company);
            c.setCreatedAt(createdAt);
            c.setAddress(address);
            return clientRepository.save(c);
        }

        existing.setName(name);
        existing.setEmail(email);
        existing.setPhone(phone);
        existing.setCompany(company);
        existing.setCreatedAt(createdAt);
        return clientRepository.save(existing);
    }


    public Task createTaskIfMissing(String title,
                                     String description,
                                     TaskStatus status,
                                     LocalDateTime start,
                                     LocalDateTime end,
                                     User createdBy,
                                     Client client,
                                     Company company) {

        List<Task> existing = taskRepository.findByTitleContainingIgnoreCaseAndDeletedIsFalse(title);
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
        task.setCompanyId(company.getId());

        return taskRepository.save(task);
    }

    public void assignIfMissing(User user, Task task, int initialMinutes) {
        boolean exists = taskAssignmentRepository
                .findByUserIdAndTaskId(user.getId(), task.getId())
                .isPresent();

        if (!exists) {
            TaskAssignment assignment = new TaskAssignment(user, task);
            assignment.setMinutesWorked(initialMinutes);
            taskAssignmentRepository.save(assignment);
        }
    }

    public Todo createTodoIfMissing(Task task,
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

    public Material createMaterialIfMissing(Task task,
                                             String description,
                                             float quantity,
                                             float unitPrice) {
        List<Material> existing = materialRepository.findByTaskIdOrderByIdAsc(task.getId());
        for (Material m : existing) {
            if (m.getDescription() != null && m.getDescription().equalsIgnoreCase(description)) {
                m.setQuantity(quantity);
                m.setUnitPrice(unitPrice);
                return materialRepository.save(m);
            }
        }

        Material material = new Material();
        material.setTask(task);
        material.setDescription(description);
        material.setQuantity(quantity);
        material.setUnitPrice(unitPrice);

        return materialRepository.save(material);
    }

    public Comment createCommentIfMissing(Task task,
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
