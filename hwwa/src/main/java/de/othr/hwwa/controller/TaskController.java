package de.othr.hwwa.controller;

import de.othr.hwwa.service.TaskServiceI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskController {

    private final TaskServiceI taskService;

    public TaskController(TaskServiceI taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("tasks", taskService.getAssignedTasksForUser());
        return "tasks";
    }
}