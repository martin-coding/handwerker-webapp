package de.othr.hwwa.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskStatus;
import de.othr.hwwa.service.EmployeeServiceI;
import de.othr.hwwa.service.TaskServiceI;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final EmployeeServiceI employeeService;
    private final TaskServiceI taskService;

    public DashboardController(EmployeeServiceI employeeService,
                               TaskServiceI taskService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
    }

    @GetMapping
    public String showDashboard(
            @RequestParam(defaultValue = "present") String tab,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "startDateTime") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {

        Sort sortObj = dir.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        Pageable pageable = PageRequest.of(page, size, sortObj);

        List<TaskStatus> presentStatuses = List.of(
            TaskStatus.PLANNED,
            TaskStatus.IN_PROGRESS
        );

        List<TaskStatus> pastStatuses = List.of(
            TaskStatus.DONE,
            TaskStatus.CANCELED
        );

        List<TaskStatus> statuses =
            tab.equals("past") ? pastStatuses : presentStatuses;

        Page<Task> taskPage = taskService.findAllTasks(pageable, statuses);

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("taskPage", taskPage);

        model.addAttribute("currentPage", taskPage.getNumber());
        model.addAttribute("totalPages", taskPage.getTotalPages());

        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("activeTab", tab);

        model.addAttribute("taskCountPlanned",
            taskService.countByStatus(TaskStatus.PLANNED));

        model.addAttribute("taskCountInProgress",
                taskService.countByStatus(TaskStatus.IN_PROGRESS));

        model.addAttribute("taskCountDone",
                taskService.countByStatus(TaskStatus.DONE));

        model.addAttribute("taskCountCanceled",
                taskService.countByStatus(TaskStatus.CANCELED));

        return "dashboard";
    }
}

