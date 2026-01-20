package de.othr.hwwa.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import de.othr.hwwa.model.Material;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.User;
import de.othr.hwwa.service.MaterialServiceI;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.service.PdfServiceI;
import de.othr.hwwa.service.TaskServiceI;
import org.springframework.stereotype.Service;
import java.awt.*;
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

    public byte[] generateInvoicePdf(InvoiceDto invoice) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);


        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{60, 40});

        PdfPCell companyCell = new PdfPCell();
        companyCell.setBorder(Rectangle.NO_BORDER);
        companyCell.addElement(new Paragraph(invoice.getCompanyName(), headerFont));
        companyCell.addElement(new Paragraph(invoice.getCompanyAddress().getStreet(), normalFont));
        companyCell.addElement(new Paragraph(
                invoice.getCompanyAddress().getCity() + ", " +
                        invoice.getCompanyAddress().getPostalCode(), normalFont));
        companyCell.addElement(new Paragraph(invoice.getCreatedByEmail(), normalFont));

        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.setBorder(Rectangle.NO_BORDER);
        invoiceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceCell.addElement(new Paragraph("Rechnung", titleFont));
        invoiceCell.addElement(new Paragraph("Referenz: INV-" + invoice.getId(), normalFont));
        invoiceCell.addElement(new Paragraph("Erstellt am: " + invoice.getInvoiceCreationDate(), normalFont));
        invoiceCell.addElement(new Paragraph("Fällig am: " + invoice.getInvoiceIssuedDate(), normalFont));

        headerTable.addCell(companyCell);
        headerTable.addCell(invoiceCell);

        document.add(headerTable);
        document.add(Chunk.NEWLINE);


        document.add(new Paragraph("Rechnung an", headerFont));
        document.add(new Paragraph(invoice.getClientName(), normalFont));
        document.add(new Paragraph(invoice.getClientAddress().getStreet(), normalFont));
        document.add(new Paragraph(
                invoice.getClientAddress().getCity() + ", " +
                        invoice.getClientAddress().getPostalCode(), normalFont));

        document.add(Chunk.NEWLINE);


        document.add(new Paragraph(
                "Job Referenz: " + invoice.getTaskReference(), boldFont));

        document.add(Chunk.NEWLINE);


        document.add(new Paragraph("Arbeitskosten", headerFont));
        document.add(new Paragraph(" ", headerFont));
        PdfPTable workTable = new PdfPTable(4);
        workTable.setWidthPercentage(100);
        workTable.setWidths(new float[]{50, 15, 15, 20});

        addTableHeader(workTable, "Beschreibung", "Stunden", "Stundenlohn", "Gesamtsumme");

        invoice.getWorkCosts().forEach(w -> {
            addTableCell(workTable, w.getDescription());
            addTableCell(workTable, ((Float)w.getHoursWorked()).toString());
            addTableCell(workTable, ((Float)w.getHourlyRate()).toString());
            addTableCell(workTable, ((Float)w.getTotal()).toString());
        });
        document.add(workTable);
        document.add(Chunk.NEWLINE);


        document.add(new Paragraph("Verwendete Materialen", headerFont));
        document.add(new Paragraph(" ", headerFont));
        PdfPTable materialTable = new PdfPTable(4);
        materialTable.setWidthPercentage(100);
        materialTable.setWidths(new float[]{50, 15, 15, 20});

        addTableHeader(materialTable, "Beschreibung", "Anzahl", "Stückpreis", "Gesamtsumme");

        invoice.getMaterials().forEach(m -> {
            addTableCell(materialTable, m.getDescription());
            addTableCell(materialTable, ((Float)m.getCount()).toString());
            addTableCell(materialTable, ((Float)m.getPricePerStock()).toString());
            addTableCell(materialTable, ((Float)m.getTotal()).toString());
        });
        document.add(materialTable);
        document.add(Chunk.NEWLINE);

        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.setWidths(new float[]{60, 40});

        PdfPCell totalLabel = new PdfPCell(new Paragraph("Gesamtsumme", boldFont));
        totalLabel.setBorder(Rectangle.NO_BORDER);

        PdfPCell totalValue = new PdfPCell(
                new Paragraph(formatMoney(invoice.getTotal()), boldFont));
        totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValue.setBorder(Rectangle.NO_BORDER);

        totalTable.addCell(totalLabel);
        totalTable.addCell(totalValue);

        document.add(totalTable);

        document.close();

        return outputStream.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(6);
            table.addCell(cell);
        }
    }

    private void addTableCell(PdfPTable table, String value) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(value, font));
        cell.setPadding(6);
        table.addCell(cell);
    }

    private String formatMoney(float value) {
        return "€" + value;
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

        table.addCell(headerCell("Beschreibung"));
        table.addCell(headerCell("Menge"));
        table.addCell(headerCell("Preis/Einheit"));

        if (materials == null || materials.isEmpty()) {
            PdfPCell empty = new PdfPCell(new Phrase("Keine Materialien erfasst."));
            empty.setColspan(3);
            empty.setPadding(8);
            table.addCell(empty);
        } else {
            for (Material m : materials) {
                table.addCell(bodyCell(safe(m.getDescription())));
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