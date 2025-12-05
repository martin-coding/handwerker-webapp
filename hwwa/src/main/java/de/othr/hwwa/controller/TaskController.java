package de.othr.hwwa.controller;

import org.springframework.stereotype.Controller;

import de.othr.hwwa.service.TaskServiceI;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskController {

    private TaskServiceI taskService;

    public TaskController(TaskServiceI taskService) {
        super();
        this.taskService= taskService;
    }

    @GetMapping(value = {"/tasks"})
    public String tasks(Model model) {
        taskService.getAllTasks().forEach(task -> {
            System.out.println(task);
        });
        return "home";
    }
}