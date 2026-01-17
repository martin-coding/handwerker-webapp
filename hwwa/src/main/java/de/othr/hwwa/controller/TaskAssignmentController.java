package de.othr.hwwa.controller;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.model.Company;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.TaskAssignmentCreateDto;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.TaskServiceI;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskAssignmentController {

    private final TaskServiceI taskService;
    private final UserRepositoryI userRepository;

    public TaskAssignmentController(TaskServiceI taskService, UserRepositoryI userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((MyUserDetails) principal).getLoggedInUser();
    }

    private Company getCurrentCompany() {
        return getCurrentUser().getCompany();
    }

    private boolean canManageTasks() {
        String roleName = getCurrentUser().getRole() != null ? getCurrentUser().getRole().getName() : null;
        return "Owner".equalsIgnoreCase(roleName) || "Manager".equalsIgnoreCase(roleName);
    }

    private List<User> loadActiveUsersForCurrentCompany() {
        return userRepository.findByCompanyIdAndActiveTrueOrderByLastNameAscFirstNameAsc(getCurrentCompany().getId());
    }

    @GetMapping("/tasks/{taskId}/assignments/add")
    public String addAssignmentForm(@PathVariable long taskId, Model model) {
        if (!canManageTasks()) throw new AccessDeniedException("Access denied");

        Task task = taskService.getAssignedTaskById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        model.addAttribute("task", task);
        model.addAttribute("assignmentCreate", new TaskAssignmentCreateDto());
        model.addAttribute("users", loadActiveUsersForCurrentCompany());
        return "task/task_assignment_add";
    }

    @PostMapping("/tasks/{taskId}/assignments/add")
    public String addAssignmentSubmit(@PathVariable long taskId,
                                      @Valid @ModelAttribute("assignmentCreate") TaskAssignmentCreateDto dto,
                                      BindingResult bindingResult,
                                      Model model) {
        if (!canManageTasks()) throw new AccessDeniedException("Access denied");

        Task task = taskService.getAssignedTaskById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            model.addAttribute("users", loadActiveUsersForCurrentCompany());
            return "task/task_assignment_add";
        }

        taskService.assignUserToTask(taskId, dto.getUserId(), dto.getInitialMinutes());
        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/tasks/{taskId}/assignments/{userId}/delete")
    public String deleteAssignment(@PathVariable long taskId, @PathVariable long userId) {
        if (!canManageTasks()) throw new AccessDeniedException("Access denied");

        taskService.unassignUserFromTask(taskId, userId);
        return "redirect:/tasks/" + taskId;
    }
}