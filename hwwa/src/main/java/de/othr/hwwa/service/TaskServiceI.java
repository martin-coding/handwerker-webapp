package de.othr.hwwa.service;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.User;

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

    void assignUserToTask(User user, Task task, int initialMinutes);

    void addWorkHours(Long taskId, Long userId, int minutes);
}