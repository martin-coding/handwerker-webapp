package de.othr.hwwa.service;

import de.othr.hwwa.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoServiceI {
    List<Todo> getTodosForTask(long taskId);
    Optional<Todo> getTodoById(long id);
    Todo save(Todo todo);
    void deleteById(long id);
}