package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskAssignment;
import de.othr.hwwa.model.User;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.repository.TaskRepositoryI;
import de.othr.hwwa.service.TaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl extends SecurityServiceImpl implements TaskServiceI {

    private final TaskRepositoryI taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    public TaskServiceImpl(TaskRepositoryI taskRepository,
                           TaskAssignmentRepository taskAssignmentRepository) {
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
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
        if (!taskRepository.existsById(task.getId())) {
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
        return taskAssignmentRepository.findByUserId(getCurrentUserId())
                .stream()
                .map(TaskAssignment::getTask)
                .toList();
    }

    @Override
    @Transactional
    public void assignUserToTask(User user, Task task, int initialMinutes) {
        if (taskAssignmentRepository.findByUserIdAndTaskId(user.getId(), task.getId()).isPresent()) {
            throw new IllegalStateException("User already assigned to task");
        }
        TaskAssignment assignment = new TaskAssignment(user, task);
        assignment.setMinutesWorked(initialMinutes);
        taskAssignmentRepository.save(assignment);
    }

    @Override
    @Transactional
    public void addWorkHours(Long taskId, Long userId, int minutes) {
        TaskAssignment assignment = taskAssignmentRepository.findByUserIdAndTaskId(userId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("No assignment found"));
        assignment.setMinutesWorked(assignment.getMinutesWorked() + minutes);
        taskAssignmentRepository.save(assignment);
    }
}