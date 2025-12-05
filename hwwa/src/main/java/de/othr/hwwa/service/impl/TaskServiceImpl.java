package de.othr.hwwa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.repository.TaskRepositoryI;
import de.othr.hwwa.service.TaskServiceI;


@Service
public class TaskServiceImpl implements TaskServiceI{


    private TaskRepositoryI  taskRepository;

    public TaskServiceImpl(TaskRepositoryI  taskRepository) {
        super();

        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public Task saveTask(Task task) {
        return null;
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id) ;
    }

    @Override
    public Task updateTask(Task task) {
        return null;
    }

    @Override
    public void delete(Task Task) {

    }

    @Override
    public List<Task> findTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

}
