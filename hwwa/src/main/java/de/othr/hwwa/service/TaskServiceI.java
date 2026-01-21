package de.othr.hwwa.service;

import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.TaskCreateDto;
import de.othr.hwwa.model.dto.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskServiceI {

    Page<Task> findAllTasks(Pageable pageable, Collection<TaskStatus> statuses);

    Long countByStatus(TaskStatus status);

    void delete(Task task);

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

    Coordinates getTaskCoordinates(Long taskId);

    Page<Task> getTasksPagedForCurrentUser(String keyword, Collection<TaskStatus> statuses, Pageable pageable);
}