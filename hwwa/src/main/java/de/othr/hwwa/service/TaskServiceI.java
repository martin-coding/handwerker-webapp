package de.othr.hwwa.service;

import java.util.List;
import java.util.Optional;

import de.othr.hwwa.model.Task;

public interface TaskServiceI {

    List<Task> getAllTasks();

    Task saveTask(Task task);

    Optional <Task> getTaskById(Long id);

    Task updateTask(Task task);

    void delete(Task task);

    public List<Task> findTasksByTitle(String title) ;

}
