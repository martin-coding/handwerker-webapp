package de.othr.hwwa.controller;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.model.Client;
import de.othr.hwwa.model.Company;
import de.othr.hwwa.model.Coordinates;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskStatus;
import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.CommentCreateDto;
import de.othr.hwwa.model.dto.TaskCreateDto;
import de.othr.hwwa.model.dto.TaskUpdateDto;
import de.othr.hwwa.model.dto.WeatherDto;
import de.othr.hwwa.repository.ClientRepositoryI;
import de.othr.hwwa.service.CommentServiceI;
import de.othr.hwwa.service.MaterialServiceI;
import de.othr.hwwa.service.TaskServiceI;
import de.othr.hwwa.service.TodoServiceI;
import de.othr.hwwa.service.WeatherServiceI;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
public class TaskController {

    private final TaskServiceI taskService;
    private final TodoServiceI todoService;
    private final MaterialServiceI materialService;
    private final ClientRepositoryI clientRepository;
    private final CommentServiceI commentService;
    private final WeatherServiceI weatherService;

    public TaskController(TaskServiceI taskService,
                          TodoServiceI todoService,
                          MaterialServiceI materialService,
                          ClientRepositoryI clientRepository,
                          CommentServiceI commentService,
                          WeatherServiceI weatherService) {
        this.taskService = taskService;
        this.todoService = todoService;
        this.materialService = materialService;
        this.clientRepository = clientRepository;
        this.commentService = commentService;
        this.weatherService = weatherService;
    }

    private void validateTimeRange(TaskCreateDto dto, BindingResult bindingResult) {
        if (dto.getStartDateTime() == null && dto.getEndDateTime() == null) return;

        if (dto.getStartDateTime() == null) {
            bindingResult.rejectValue("startDateTime", "invalid", "Start muss gesetzt sein, wenn Ende gesetzt ist.");
            return;
        }
        if (dto.getEndDateTime() == null) {
            bindingResult.rejectValue("endDateTime", "invalid", "Ende muss gesetzt sein, wenn Start gesetzt ist.");
            return;
        }
        if (!dto.getStartDateTime().isBefore(dto.getEndDateTime())) {
            bindingResult.rejectValue("endDateTime", "invalid", "Ende muss nach Start liegen.");
        }
    }

    private void validateTimeRange(TaskUpdateDto dto, BindingResult bindingResult) {
        if (dto.getStartDateTime() == null && dto.getEndDateTime() == null) return;

        if (dto.getStartDateTime() == null) {
            bindingResult.rejectValue("startDateTime", "invalid", "Start muss gesetzt sein, wenn Ende gesetzt ist.");
            return;
        }
        if (dto.getEndDateTime() == null) {
            bindingResult.rejectValue("endDateTime", "invalid", "Ende muss gesetzt sein, wenn Start gesetzt ist.");
            return;
        }
        if (!dto.getStartDateTime().isBefore(dto.getEndDateTime())) {
            bindingResult.rejectValue("endDateTime", "invalid", "Ende muss nach Start liegen.");
        }
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

    private String normalizeTab(String tab) {
        if (tab == null) return "present";
        String t = tab.trim().toLowerCase();
        if ("present".equals(t) || "past".equals(t) || "all".equals(t)) return t;
        return "present";
    }

    private Collection<TaskStatus> statusesForTab(String tab) {
        return switch (tab) {
            case "past" -> List.of(TaskStatus.DONE, TaskStatus.CANCELED);
            case "all" -> List.of(TaskStatus.values());
            default -> List.of(TaskStatus.PLANNED, TaskStatus.IN_PROGRESS);
        };
    }

    private Sort sortForTab(String tab) {
        return switch (tab) {
            case "past" -> Sort.by(Sort.Order.desc("endDateTime"), Sort.Order.desc("id"));
            case "all" -> Sort.by(Sort.Order.desc("id"));
            default -> Sort.by(Sort.Order.desc("startDateTime"), Sort.Order.desc("id"));
        };
    }

    @GetMapping("/tasks")
    public String tasks(@RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "tab", defaultValue = "present") String tab,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        Model model) {

        String activeTab = normalizeTab(tab);
        Collection<TaskStatus> statuses = statusesForTab(activeTab);
        Sort sort = sortForTab(activeTab);

        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Task> tasksPage = taskService.getTasksPagedForCurrentUser(keyword, statuses, pageable);

        model.addAttribute("tasksPage", tasksPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tasksPage.getTotalPages());
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

        validateTimeRange(dto, bindingResult);

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
    public String taskDetails(@PathVariable long id, Model model) {
        Task task = taskService.getAssignedTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found " + id));

        Coordinates coordinates = taskService.getTaskCoordinates(task.getId());
        WeatherDto weather = weatherService.getWeather(coordinates);

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
        model.addAttribute("assignments", taskService.getAssignmentsForTask(id));
        model.addAttribute("comments", commentService.getCommentsForTask(id));
        model.addAttribute("commentCreate", new CommentCreateDto());
        model.addAttribute("currentUserId", getCurrentUser().getId());
        model.addAttribute("weather", weather);

        return "task/task_details";
    }

    @PostMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable long id,
                           @Valid @ModelAttribute("taskUpdate") TaskUpdateDto dto,
                           BindingResult bindingResult,
                           Model model) {

        if (!canManageTasks()) {
            throw new AccessDeniedException("Access denied");
        }

        validateTimeRange(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            Task task = taskService.getAssignedTaskById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found " + id));

            model.addAttribute("task", task);
            model.addAttribute("todos", todoService.getTodosForTask(id));
            model.addAttribute("materials", materialService.getMaterialsForTask(id));
            model.addAttribute("clients", loadClientsForCurrentCompany());
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("canManageTasks", true);
            model.addAttribute("assignments", taskService.getAssignmentsForTask(id));
            model.addAttribute("comments", commentService.getCommentsForTask(id));
            model.addAttribute("commentCreate", new CommentCreateDto());
            model.addAttribute("currentUserId", getCurrentUser().getId());

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