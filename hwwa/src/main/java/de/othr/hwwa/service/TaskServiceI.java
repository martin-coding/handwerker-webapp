package de.othr.hwwa.service;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskAssignment;
import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.TaskCreateDto;
import de.othr.hwwa.model.dto.TaskUpdateDto;

import java.util.List;
import java.util.Optional;

public interface TaskServiceI {

    List<Task> getAllTasks();

    Task saveTask(Task task);

    Optional<Task> getTaskById(Long id);

    Task updateTask(Task task);

    void delete(Task task);

    List<Task> findTasksByTitle(String title);

    List<Task> getAssignedTasksForUser();

    Optional<Task> getAssignedTaskById(Long taskId);

    Optional<Task> getTaskForMaterialTodoAccess(Long taskId);

    Task createTask(TaskCreateDto dto);

    Task updateAssignedTask(Long taskId, TaskUpdateDto dto);

    void deleteAssignedTask(Long taskId);

    void addWorkHours(Long taskId, Long userId, int minutes);

    List<TaskAssignment> getAssignmentsForTask(Long taskId);

    void assignUserToTask(Long taskId, Long userId, int initialMinutes);

    void unassignUserFromTask(Long taskId, Long userId);

    void assignUserToTask(User user, Task task, int initialMinutes);
}