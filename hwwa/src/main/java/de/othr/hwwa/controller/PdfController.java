package de.othr.hwwa.controller;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.service.PdfServiceI;
import de.othr.hwwa.service.TaskServiceI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PdfController {

    private final TaskServiceI taskService;
    private final PdfServiceI pdfService;

    public PdfController(TaskServiceI taskService, PdfServiceI pdfService) {
        this.taskService = taskService;
        this.pdfService = pdfService;
    }

    @GetMapping("/tasks/{id}/pdf")
    public ResponseEntity<byte[]> downloadTaskPdf(@PathVariable("id") long id) {
        Task task = taskService.getAssignedTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        byte[] pdf = pdfService.buildTaskPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=task-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}