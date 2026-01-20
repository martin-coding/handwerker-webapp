package de.othr.hwwa.service.impl;

import de.othr.hwwa.event.AllTodosDoneEvent;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.Todo;
import de.othr.hwwa.model.User;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.repository.TaskRepositoryI;
import de.othr.hwwa.repository.TodoRepositoryI;
import de.othr.hwwa.service.EmailServiceI;
import de.othr.hwwa.service.TodoServiceI;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TodoServiceImpl extends SecurityServiceImpl implements TodoServiceI {

    private final TodoRepositoryI todoRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final TaskRepositoryI taskRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TodoServiceImpl(TodoRepositoryI todoRepository,
                           TaskAssignmentRepository taskAssignmentRepository,
                           TaskRepositoryI taskRepository, EmailServiceI emailService, ApplicationEventPublisher eventPublisher) {
        this.todoRepository = todoRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
    }

    private boolean isOwnerOrManager() {
        String roleName = getCurrentUser().getRole() != null ? getCurrentUser().getRole().getName() : null;
        return "Owner".equalsIgnoreCase(roleName) || "Manager".equalsIgnoreCase(roleName);
    }

    private void assertCanAccessTask(long taskId) {
        if (isOwnerOrManager()) {
            Task task = taskRepository.findByIdAndDeletedIsFalse(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

            Long currentCompanyId = getCurrentCompany().getId();
            Long taskCompanyId = task.getClient() != null && task.getClient().getCompany() != null
                    ? task.getClient().getCompany().getId()
                    : null;

            if (taskCompanyId == null || !taskCompanyId.equals(currentCompanyId)) {
                throw new AccessDeniedException("Access denied");
            }
            return;
        }

        boolean assigned = taskAssignmentRepository
                .findByUserIdAndTaskId(getCurrentUserId(), taskId)
                .isPresent();

        if (!assigned) {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public List<Todo> getTodosForTask(long taskId) {
        assertCanAccessTask(taskId);
        return todoRepository.findByTaskIdOrderByDoneAscIdAsc(taskId);
    }

    @Override
    public Optional<Todo> getTodoById(long id) {
        Optional<Todo> todoOpt = todoRepository.findById(id);
        todoOpt.ifPresent(t -> assertCanAccessTask(t.getTask().getId()));
        return todoOpt;
    }

@Override
    @Transactional
    public Todo save(Todo todo) {

        if (todo.getTask() == null) {
            throw new IllegalArgumentException("Todo.task must not be null");
        }

        Task task = todo.getTask();
        Long taskId = task.getId();
        assertCanAccessTask(taskId);

        Todo saved = todoRepository.save(todo);

        String task_name = task.getTitle();

        boolean allDone =
            !todoRepository.existsByTaskIdAndDoneFalse(taskId);

        // Extract needed data while session is open
        if (allDone) {
            List<String> emails =
                saved.getTask().getAssignedUsers()
                     .stream()
                     .map(User::getEmail)
                     .toList();

            // Publish event inside transaction
            eventPublisher.publishEvent(
                new AllTodosDoneEvent(emails, task_name)
            );
        }
        return saved;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + id));

        assertCanAccessTask(todo.getTask().getId());
        todoRepository.deleteById(id);
    }
}