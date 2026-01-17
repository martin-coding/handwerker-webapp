package de.othr.hwwa.controller;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.Todo;
import de.othr.hwwa.model.dto.TodoCreateDto;
import de.othr.hwwa.model.dto.TodoUpdateDto;
import de.othr.hwwa.service.TaskServiceI;
import de.othr.hwwa.service.TodoServiceI;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoController {

    private final TodoServiceI todoService;
    private final TaskServiceI taskService;

    public TodoController(TodoServiceI todoService, TaskServiceI taskService) {
        this.todoService = todoService;
        this.taskService = taskService;
    }

    private Task loadTaskForAccess(long taskId) {
        return taskService.getTaskForMaterialTodoAccess(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
    }

    @GetMapping("/tasks/{taskId}/todos/add")
    public String addTodoForm(@PathVariable long taskId, Model model) {
        Task task = loadTaskForAccess(taskId);

        model.addAttribute("task", task);
        model.addAttribute("todoCreate", new TodoCreateDto());
        return "task/todo_add";
    }

    @PostMapping("/tasks/{taskId}/todos/add")
    public String addTodoSubmit(@PathVariable long taskId,
                                @Valid @ModelAttribute("todoCreate") TodoCreateDto dto,
                                BindingResult bindingResult,
                                Model model) {

        Task task = loadTaskForAccess(taskId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            return "task/todo_add";
        }

        Todo t = new Todo();
        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());
        t.setDone(false);
        t.setTask(task);

        Todo saved = todoService.save(t);
        return "redirect:/tasks/" + taskId + "/todos/" + saved.getId();
    }

    @GetMapping("/tasks/{taskId}/todos/{todoId}")
    public String todoDetails(@PathVariable long taskId,
                              @PathVariable long todoId,
                              Model model) {

        Task task = loadTaskForAccess(taskId);

        Todo todo = todoService.getTodoById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + todoId));

        if (todo.getTask() == null || todo.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Todo does not belong to task: " + taskId);
        }

        TodoUpdateDto updateDto = new TodoUpdateDto();
        updateDto.setTitle(todo.getTitle());
        updateDto.setDescription(todo.getDescription());
        updateDto.setDone(todo.isDone());

        model.addAttribute("task", task);
        model.addAttribute("todo", todo);
        model.addAttribute("todoUpdate", updateDto);
        return "task/todo_details";
    }

    @PostMapping("/tasks/{taskId}/todos/{todoId}/edit")
    public String editTodo(@PathVariable long taskId,
                           @PathVariable long todoId,
                           @Valid @ModelAttribute("todoUpdate") TodoUpdateDto dto,
                           BindingResult bindingResult,
                           Model model) {

        Task task = loadTaskForAccess(taskId);

        Todo todo = todoService.getTodoById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + todoId));

        if (todo.getTask() == null || todo.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Todo does not belong to task: " + taskId);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            model.addAttribute("todo", todo);
            return "task/todo_details";
        }

        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setDone(dto.isDone());

        todoService.save(todo);
        return "redirect:/tasks/" + taskId + "/todos/" + todoId;
    }

    @PostMapping("/tasks/{taskId}/todos/{todoId}/toggle")
    public String toggleTodo(@PathVariable long taskId, @PathVariable long todoId) {
        Todo todo = todoService.getTodoById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + todoId));

        if (todo.getTask() == null || todo.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Todo does not belong to task: " + taskId);
        }

        todo.setDone(!todo.isDone());
        todoService.save(todo);

        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/tasks/{taskId}/todos/{todoId}/delete")
    public String deleteTodo(@PathVariable long taskId, @PathVariable long todoId) {
        Todo todo = todoService.getTodoById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + todoId));

        if (todo.getTask() == null || todo.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Todo does not belong to task: " + taskId);
        }

        todoService.deleteById(todoId);
        return "redirect:/tasks/" + taskId;
    }
}