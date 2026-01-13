package de.othr.hwwa.controller;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.model.User;
import de.othr.hwwa.service.TaskServiceI;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String tasks(Model model, @AuthenticationPrincipal MyUserDetails principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User currentUser = principal.getLoggedInUser();

        model.addAttribute("tasks", taskService.getAssignedTasksForUser(currentUser));
        return "tasks";
    }
}