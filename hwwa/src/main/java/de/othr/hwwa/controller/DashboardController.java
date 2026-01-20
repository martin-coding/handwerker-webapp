package de.othr.hwwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String showDashboard(Model model) {

        // model.addAttribute("employees", employeeService.findAll());
        // model.addAttribute("activeTasks", taskService.findActiveTasks());
        // model.addAttribute("completedTasks", taskService.findCompletedTasks());
        // model.addAttribute("workTimes", workTimeService.findLastWorkTimes());

        return "dashboard";
    }
}
