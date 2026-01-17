package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.TaskCreateDto;
import de.othr.hwwa.model.dto.TaskUpdateDto;
import de.othr.hwwa.repository.*;
import de.othr.hwwa.service.CommentServiceI;
import de.othr.hwwa.service.MaterialServiceI;
import de.othr.hwwa.service.TaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl extends SecurityServiceImpl implements TaskServiceI {

    private final TaskRepositoryI taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final ClientRepositoryI clientRepository;
    private final UserRepositoryI userRepository;
    private final TodoRepositoryI todoRepository;
    private final CommentRepositoryI commentRepository;
    private final MaterialRepositoryI materialRepository;

    @Autowired
    public TaskServiceImpl(TaskRepositoryI taskRepository,
                           TaskAssignmentRepository taskAssignmentRepository,
                           ClientRepositoryI clientRepository,
                           UserRepositoryI userRepository, TodoRepositoryI todoRepository,
                           CommentRepositoryI commentRepository, MaterialRepositoryI materialRepository) {
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.commentRepository = commentRepository;
        this.materialRepository = materialRepository;
    }

    private boolean isOwnerOrManager() {
        String roleName = getCurrentUser().getRole() != null ? getCurrentUser().getRole().getName() : null;
        return "Owner".equalsIgnoreCase(roleName) || "Manager".equalsIgnoreCase(roleName);
    }

    private void assertCurrentUserCanManageTasks() {
        if (!isOwnerOrManager()) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private void assertCurrentUserAssignedToTask(Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task id must not be null");
        }

        boolean assigned = taskAssignmentRepository
                .findByUserIdAndTaskId(getCurrentUserId(), taskId)
                .isPresent();

        if (!assigned) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private void assertTaskBelongsToCurrentCompany(Task task) {
        Long currentCompanyId = getCurrentCompany().getId();
        Long taskCompanyId = task.getClient() != null && task.getClient().getCompany() != null
                ? task.getClient().getCompany().getId()
                : null;

        if (taskCompanyId == null || !taskCompanyId.equals(currentCompanyId)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private void assertCanAccessTaskForMaterialTodo(Task task) {
        if (isOwnerOrManager()) {
            assertTaskBelongsToCurrentCompany(task);
            return;
        }
        assertCurrentUserAssignedToTask(task.getId());
    }

    private Client loadClientForCurrentCompanyOrThrow(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + clientId));

        Long currentCompanyId = getCurrentCompany().getId();
        Long clientCompanyId = client.getCompany() != null ? client.getCompany().getId() : null;

        if (clientCompanyId == null || !clientCompanyId.equals(currentCompanyId)) {
            throw new AccessDeniedException("Access denied");
        }
        return client;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    @Transactional
    public Task updateTask(Task task) {
        if (task.getId() == null || !taskRepository.existsById(task.getId())) {
            throw new IllegalArgumentException("Task not found: " + task.getId());
        }
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public List<Task> findTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Task> getAssignedTasksForUser() {
        if (isOwnerOrManager()) {
            return taskRepository.findByClientCompanyIdOrderByIdAsc(getCurrentCompany().getId());
        }

        return taskAssignmentRepository.findByUserId(getCurrentUserId())
                .stream()
                .map(TaskAssignment::getTask)
                .toList();
    }

    @Override
    public Optional<Task> getAssignedTaskById(Long taskId) {
        if (taskId == null) {
            return Optional.empty();
        }

        if (isOwnerOrManager()) {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
            assertTaskBelongsToCurrentCompany(task);
            return Optional.of(task);
        }

        assertCurrentUserAssignedToTask(taskId);
        return taskRepository.findById(taskId);
    }

    @Override
    public Optional<Task> getTaskForMaterialTodoAccess(Long taskId) {
        if (taskId == null) {
            return Optional.empty();
        }

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        taskOpt.ifPresent(this::assertCanAccessTaskForMaterialTodo);
        return taskOpt;
    }

    @Override
    @Transactional
    public Task createTask(TaskCreateDto dto) {
        assertCurrentUserCanManageTasks();

        Client client = loadClientForCurrentCompanyOrThrow(dto.getClientId());

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setStartDateTime(dto.getStartDateTime());
        task.setEndDateTime(dto.getEndDateTime());
        task.setCreatedBy(getCurrentUser());
        task.setClient(client);

        Task saved = taskRepository.save(task);

        TaskAssignment assignment = new TaskAssignment(getCurrentUser(), saved);
        assignment.setMinutesWorked(0);
        taskAssignmentRepository.save(assignment);

        return saved;
    }

    @Override
    @Transactional
    public Task updateAssignedTask(Long taskId, TaskUpdateDto dto) {
        assertCurrentUserCanManageTasks();

        if (taskId == null) {
            throw new IllegalArgumentException("Task not found: null");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        assertTaskBelongsToCurrentCompany(task);

        Client client = loadClientForCurrentCompanyOrThrow(dto.getClientId());

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setStartDateTime(dto.getStartDateTime());
        task.setEndDateTime(dto.getEndDateTime());
        task.setClient(client);

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteAssignedTask(Long taskId) {
        assertCurrentUserCanManageTasks();

        if (taskId == null) {
            throw new IllegalArgumentException("Task not found: null");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        assertTaskBelongsToCurrentCompany(task);

        List<Material> materials = materialRepository.findByTaskIdOrderByIdAsc(taskId);
        List<Comment> comments = commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
        List<Todo> todos = todoRepository.findByTaskIdOrderByDoneAscIdAsc(taskId);

        materialRepository.deleteAll(materials);
        commentRepository.deleteAll(comments);
        todoRepository.deleteAll(todos);

        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public void addWorkHours(Long taskId, Long userId, int minutes) {
        if (taskId == null || userId == null) {
            throw new IllegalArgumentException("TaskId/UserId must not be null");
        }

        if (minutes <= 0) {
            throw new IllegalArgumentException("Minutes must be > 0");
        }

        Long currentUserId = getCurrentUserId();
        if (!currentUserId.equals(userId)) {
            throw new AccessDeniedException("Cannot book work hours for other users");
        }

        TaskAssignment assignment = taskAssignmentRepository
                .findByUserIdAndTaskId(userId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("No assignment found"));

        assignment.setMinutesWorked(assignment.getMinutesWorked() + minutes);
        taskAssignmentRepository.save(assignment);
    }

    @Override
    public List<TaskAssignment> getAssignmentsForTask(Long taskId) {
        getAssignedTaskById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        return taskAssignmentRepository.findByTaskId(taskId);
    }

    @Override
    @Transactional
    public void assignUserToTask(Long taskId, Long userId, int initialMinutes) {
        assertCurrentUserCanManageTasks();

        if (taskId == null || userId == null) {
            throw new IllegalArgumentException("TaskId/UserId must not be null");
        }

        if (initialMinutes < 0) {
            throw new IllegalArgumentException("Initial minutes must be >= 0");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        assertTaskBelongsToCurrentCompany(task);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (!user.isActive()) {
            throw new IllegalArgumentException("User is inactive: " + userId);
        }

        Long currentCompanyId = getCurrentCompany().getId();
        Long userCompanyId = user.getCompany() != null ? user.getCompany().getId() : null;
        if (userCompanyId == null || !userCompanyId.equals(currentCompanyId)) {
            throw new AccessDeniedException("Access denied");
        }

        if (taskAssignmentRepository.findByUserIdAndTaskId(userId, taskId).isPresent()) {
            throw new IllegalStateException("User already assigned to task");
        }

        TaskAssignment assignment = new TaskAssignment(user, task);
        assignment.setMinutesWorked(initialMinutes);
        taskAssignmentRepository.save(assignment);
    }

    @Override
    @Transactional
    public void unassignUserFromTask(Long taskId, Long userId) {
        assertCurrentUserCanManageTasks();

        if (taskId == null || userId == null) {
            throw new IllegalArgumentException("TaskId/UserId must not be null");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        assertTaskBelongsToCurrentCompany(task);

        TaskAssignmentId id = new TaskAssignmentId(userId, taskId);
        if (!taskAssignmentRepository.existsById(id)) {
            throw new IllegalArgumentException("No assignment found");
        }
        taskAssignmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void assignUserToTask(User user, Task task, int initialMinutes) {
        if (user == null || task == null) {
            throw new IllegalArgumentException("User/Task must not be null");
        }

        if (initialMinutes < 0) {
            throw new IllegalArgumentException("Initial minutes must be >= 0");
        }

        if (user.getId() == null || task.getId() == null) {
            throw new IllegalArgumentException("User/Task must be persisted (id != null)");
        }

        if (taskAssignmentRepository.findByUserIdAndTaskId(user.getId(), task.getId()).isPresent()) {
            throw new IllegalStateException("User already assigned to task");
        }

        TaskAssignment assignment = new TaskAssignment(user, task);
        assignment.setMinutesWorked(initialMinutes);
        taskAssignmentRepository.save(assignment);
    }
}