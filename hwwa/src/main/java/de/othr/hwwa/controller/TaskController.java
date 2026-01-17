package de.othr.hwwa.controller;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.model.Client;
import de.othr.hwwa.model.Company;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskStatus;
import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.TaskCreateDto;
import de.othr.hwwa.model.dto.TaskUpdateDto;
import de.othr.hwwa.repository.ClientRepositoryI;
import de.othr.hwwa.service.MaterialServiceI;
import de.othr.hwwa.service.TaskServiceI;
import de.othr.hwwa.service.TodoServiceI;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskController {

    private final TaskServiceI taskService;
    private final TodoServiceI todoService;
    private final MaterialServiceI materialService;
    private final ClientRepositoryI clientRepository;

    public TaskController(TaskServiceI taskService,
                          TodoServiceI todoService,
                          MaterialServiceI materialService,
                          ClientRepositoryI clientRepository) {
        this.taskService = taskService;
        this.todoService = todoService;
        this.materialService = materialService;
        this.clientRepository = clientRepository;
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

    private List<Client> loadClientsForCurrentCompany() {
        return clientRepository.findByCompanyIdOrderByNameAsc(getCurrentCompany().getId());
    }

    @GetMapping("/tasks")
    public String tasks(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Task> tasks = taskService.getAssignedTasksForUser();

        if (keyword != null && !keyword.isBlank()) {
            String k = keyword.trim().toLowerCase();
            tasks = tasks.stream()
                    .filter(t -> t.getTitle() != null && t.getTitle().toLowerCase().contains(k))
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("canManageTasks", canManageTasks());
        return "task/tasks";
    }

    @GetMapping("/tasks/add")
    public String addTaskForm(Model model) {
        if (!canManageTasks()) {
            throw new AccessDeniedException("Access denied");
        }

        model.addAttribute("taskCreate", new TaskCreateDto());
        model.addAttribute("clients", loadClientsForCurrentCompany());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("canManageTasks", true);
        return "task/task_add";
    }

    @PostMapping("/tasks/add")
    public String addTaskSubmit(@Valid @ModelAttribute("taskCreate") TaskCreateDto dto,
                                BindingResult bindingResult,
                                Model model) {

        if (!canManageTasks()) {
            throw new AccessDeniedException("Access denied");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("clients", loadClientsForCurrentCompany());
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("canManageTasks", true);
            return "task/task_add";
        }

        Task created = taskService.createTask(dto);
        return "redirect:/tasks/" + created.getId();
    }

    @GetMapping("/tasks/{id}")
    public String taskDetails(@PathVariable("id") long id, Model model) {
        Task task = taskService.getAssignedTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        TaskUpdateDto updateDto = new TaskUpdateDto();
        updateDto.setTitle(task.getTitle());
        updateDto.setDescription(task.getDescription());
        updateDto.setStatus(task.getStatus());
        updateDto.setClientId(task.getClient() != null ? task.getClient().getId() : null);
        updateDto.setStartDateTime(task.getStartDateTime());
        updateDto.setEndDateTime(task.getEndDateTime());

        model.addAttribute("task", task);
        model.addAttribute("taskUpdate", updateDto);
        model.addAttribute("todos", todoService.getTodosForTask(id));
        model.addAttribute("materials", materialService.getMaterialsForTask(id));
        model.addAttribute("clients", loadClientsForCurrentCompany());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("canManageTasks", canManageTasks());

        return "task/task_details";
    }

    @PostMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable("id") long id,
                           @Valid @ModelAttribute("taskUpdate") TaskUpdateDto dto,
                           BindingResult bindingResult,
                           Model model) {

        if (!canManageTasks()) {
            throw new AccessDeniedException("Access denied");
        }

        if (bindingResult.hasErrors()) {
            Task task = taskService.getAssignedTaskById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

            model.addAttribute("task", task);
            model.addAttribute("todos", todoService.getTodosForTask(id));
            model.addAttribute("materials", materialService.getMaterialsForTask(id));
            model.addAttribute("clients", loadClientsForCurrentCompany());
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("canManageTasks", true);
            return "task/task_details";
        }

        taskService.updateAssignedTask(id, dto);
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") long id) {
        if (!canManageTasks()) {
            throw new AccessDeniedException("Access denied");
        }
        taskService.deleteAssignedTask(id);
        return "redirect:/tasks";
    }
}
