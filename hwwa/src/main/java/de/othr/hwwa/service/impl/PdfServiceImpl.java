package de.othr.hwwa.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import de.othr.hwwa.model.Material;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.User;
import de.othr.hwwa.service.MaterialServiceI;
import de.othr.hwwa.service.PdfServiceI;
import de.othr.hwwa.service.TaskServiceI;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfServiceImpl extends SecurityServiceImpl implements PdfServiceI {

    private final TaskServiceI taskService;
    private final MaterialServiceI materialService;

    public PdfServiceImpl(TaskServiceI taskService, MaterialServiceI materialService) {
        this.taskService = taskService;
        this.materialService = materialService;
    }

    @Override
    public byte[] buildTaskPdf(long taskId) {
        User currentUser = getCurrentUser();
        Task task = taskService.getAssignedTaskById(taskId).orElse(null);
        List<Material> materials = materialService.getMaterialsForTask(taskId);

        if (task == null) {
            throw new RuntimeException("Task with id " + taskId + " not found");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document doc = new Document(PageSize.A4, 36, 36, 48, 36);
        PdfWriter.getInstance(doc, baos);

        doc.open();

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font hFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        doc.add(new Paragraph("Task Report", titleFont));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Export für: " + safe(currentUser.getLastName()) + ", " + safe(currentUser.getFirstName())));
        doc.add(new Paragraph("Task-ID: " + task.getId()));
        doc.add(new Paragraph("Titel: " + safe(task.getTitle())));
        doc.add(new Paragraph("Status: " + (task.getStatus() != null ? task.getStatus().name() : "-")));

        if (task.getStartDateTime() != null && task.getEndDateTime() != null) {
            doc.add(new Paragraph("Zeitraum: " + task.getStartDateTime() + " - " + task.getEndDateTime()));
        } else {
            doc.add(new Paragraph("Zeitraum: -"));
        }

        if (task.getClient() != null) {
            doc.add(new Paragraph("Kunde: " + safe(task.getClient().getName())));
        }

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Beschreibung", hFont));
        doc.add(new Paragraph(safe(task.getDescription())));
        doc.add(new Paragraph(" "));

        doc.add(new Paragraph("Materialliste", hFont));
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(new float[]{4f, 2f, 2f});
        table.setWidthPercentage(100);

        table.addCell(headerCell("Name"));
        table.addCell(headerCell("Menge"));
        table.addCell(headerCell("Preis/Einheit"));

        if (materials == null || materials.isEmpty()) {
            PdfPCell empty = new PdfPCell(new Phrase("Keine Materialien erfasst."));
            empty.setColspan(3);
            empty.setPadding(8);
            table.addCell(empty);
        } else {
            for (Material m : materials) {
                table.addCell(bodyCell(safe(m.getName())));
                table.addCell(bodyCell(m.getQuantity() >= 0 ? String.valueOf(m.getQuantity()) : "-"));
                table.addCell(bodyCell(m.getUnitPrice() >= 0 ? String.valueOf(m.getUnitPrice()) : "-"));
            }
        }

        doc.add(table);

        doc.close();
        return baos.toByteArray();
    }

    private PdfPCell headerCell(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text));
        c.setBackgroundColor(new Color(240, 240, 240));
        c.setPadding(6);
        return c;
    }

    private PdfPCell bodyCell(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text));
        c.setPadding(6);
        return c;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}