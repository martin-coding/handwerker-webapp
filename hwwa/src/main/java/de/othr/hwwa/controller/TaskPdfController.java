package de.othr.hwwa.controller;

import de.othr.hwwa.config.MyUserDetails;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.User;
import de.othr.hwwa.service.MaterialServiceI;
import de.othr.hwwa.service.TaskPdfServiceI;
import de.othr.hwwa.service.TaskServiceI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TaskPdfController {

    private final TaskServiceI taskService;
    private final MaterialServiceI materialService;
    private final TaskPdfServiceI taskPdfService;

    public TaskPdfController(TaskServiceI taskService, MaterialServiceI materialService, TaskPdfServiceI taskPdfService) {
        this.taskService = taskService;
        this.materialService = materialService;
        this.taskPdfService = taskPdfService;
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((MyUserDetails) principal).getLoggedInUser();
    }

    @GetMapping("/tasks/{id}/pdf")
    public ResponseEntity<byte[]> downloadTaskPdf(@PathVariable("id") long id) {
        Task task = taskService.getAssignedTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        byte[] pdf = taskPdfService.buildTaskPdf(getCurrentUser(), task, materialService.getMaterialsForTask(id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=task-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}