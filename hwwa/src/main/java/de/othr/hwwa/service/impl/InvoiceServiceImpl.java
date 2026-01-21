package de.othr.hwwa.service.impl;

import de.othr.hwwa.exceptions.InvoiceDoesNotExistException;
import de.othr.hwwa.exceptions.TaskDoesNotExistException;
import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.model.dto.InvoiceMaterialDto;
import de.othr.hwwa.model.dto.InvoiceWorkCostDto;
import de.othr.hwwa.model.dto.TaskForInvoiceDto;
import de.othr.hwwa.repository.*;
import de.othr.hwwa.service.EmailServiceI;
import de.othr.hwwa.service.InvoiceServiceI;
import de.othr.hwwa.service.PdfServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl extends SecurityServiceImpl implements InvoiceServiceI {

    private TaskRepositoryI taskRepository;
    private InvoiceRepositoryI invoiceRepository;
    private InvoiceMaterialRepositoryI invoiceMaterialRepository;
    private InvoiceWorkCostRepositoryI invoiceWorkCostRepository;
    private PdfServiceI pdfService;
    private EmailServiceI emailService;

    public InvoiceServiceImpl(TaskRepositoryI taskRepository,
                              InvoiceRepositoryI invoiceRepository,
                              InvoiceMaterialRepositoryI invoiceMaterialRepository,
                              InvoiceWorkCostRepositoryI invoiceWorkCostRepository,
                              PdfServiceI pdfService,
                              EmailServiceI emailService) {
        this.taskRepository = taskRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceMaterialRepository = invoiceMaterialRepository;
        this.invoiceWorkCostRepository = invoiceWorkCostRepository;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    public List<TaskForInvoiceDto> getAllDoneTasks() {
        Long companyId = getCurrentCompanyId();
        TaskStatus done = TaskStatus.DONE;
        List<Task> tasks = taskRepository.findByCompanyIdAndStatusAndDeletedIsFalse(companyId, done);
        List<TaskForInvoiceDto> taskForInvoiceDtos = new ArrayList<>();
        for (Task task : tasks) {
            taskForInvoiceDtos.add(new TaskForInvoiceDto(task.getId(), task.getTitle(), task.getClient().getName()));
        }
        return taskForInvoiceDtos;
    }

    public List<InvoiceDto> getAllDoneInvoices() {
        long companyId = getCurrentCompanyId();
        List<Invoice> invoices = invoiceRepository.findByCompanyId(companyId);
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceDtos.add(new InvoiceDto(
                    invoice,
                    invoiceWorkCostRepository.findInvoiceWorkCostByInvoice(invoice),
                    invoiceMaterialRepository.findInvoiceMaterialByInvoice(invoice)
            ));
        }
        return invoiceDtos;
    }

    public Task getTask(long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new TaskDoesNotExistException("Task mit dieser ID existiert nicht");
        }
        return task;
    }

    public InvoiceDto getInvoiceDataForTask(long id) {
        Task task = getTask(id);

        Invoice invoice = invoiceRepository.findInvoiceByTask(task).orElse(null);
        if (invoice != null) {
            List<InvoiceWorkCost> workCosts = invoiceWorkCostRepository.findInvoiceWorkCostByInvoice(invoice);
            List<InvoiceMaterial> invoiceMaterials = invoiceMaterialRepository.findInvoiceMaterialByInvoice(invoice);
            return new InvoiceDto(invoice, workCosts, invoiceMaterials);
        }

        float total = 0.0f;
        invoice = new Invoice(getCurrentUser(), task, getCurrentCompany(), task.getClient());
        invoice = invoiceRepository.save(invoice);

        for (Material material : task.getMaterials()) {
            InvoiceMaterial invoiceMaterial = new InvoiceMaterial(material, invoice);
            invoiceMaterialRepository.save(invoiceMaterial);
            total += invoiceMaterial.getTotal();
        }
        for (TaskAssignment assignment : task.getTaskAssignments()) {
            InvoiceWorkCost workCost = new InvoiceWorkCost(assignment, invoice);
            invoiceWorkCostRepository.save(workCost);
            total += workCost.getTotal();
        }

        invoice.setTotalAmount(total);
        invoice = invoiceRepository.save(invoice);

        List<InvoiceWorkCost> workCosts = invoiceWorkCostRepository.findInvoiceWorkCostByInvoice(invoice);
        List<InvoiceMaterial> invoiceMaterials = invoiceMaterialRepository.findInvoiceMaterialByInvoice(invoice);
        return new InvoiceDto(invoice, workCosts, invoiceMaterials);
    }

    @Transactional
    public void updateInvoice(InvoiceDto invoiceDto, long id) {
        Task task = getTask(id);
        Invoice invoice = invoiceRepository.findInvoiceByTask(task).orElse(null);
        if (invoice == null) {
            throw new InvoiceDoesNotExistException("Eine Rechnung mit dieser Id existiert nicht");
        }

        invoiceWorkCostRepository.deleteInvoiceWorkCostByInvoice(invoice);
        invoiceMaterialRepository.deleteInvoiceMaterialByInvoice(invoice);

        float total = 0.0f;

        for (InvoiceWorkCostDto workCostDto : invoiceDto.getWorkCosts()) {
            invoiceWorkCostRepository.save(new InvoiceWorkCost(workCostDto, invoice));
            total += workCostDto.getHourlyRate() * workCostDto.getHoursWorked();
        }
        for (InvoiceMaterialDto materialDto : invoiceDto.getMaterials()) {
            invoiceMaterialRepository.save(new InvoiceMaterial(materialDto, invoice));
            total += materialDto.getPricePerStock() * materialDto.getCount();
        }

        invoice.setTotalAmount(total);
        invoiceRepository.save(invoice);
    }

    @Transactional
    public void deleteInvoice(long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            throw new InvoiceDoesNotExistException("Eine Rechnung mit dieser Id existiert nicht");
        }

        invoiceWorkCostRepository.deleteInvoiceWorkCostByInvoice(invoice);
        invoiceMaterialRepository.deleteInvoiceMaterialByInvoice(invoice);
        if (invoice.getTask().isDeleted()) {
            taskRepository.delete(invoice.getTask());
        }
        invoiceRepository.delete(invoice);
    }

    public void sendInvoice(long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            throw new InvoiceDoesNotExistException("Eine Rechnung mit dieser Id existiert nicht");
        }
        List<InvoiceWorkCost> workCosts = invoiceWorkCostRepository.findInvoiceWorkCostByInvoice(invoice);
        List<InvoiceMaterial> invoiceMaterials = invoiceMaterialRepository.findInvoiceMaterialByInvoice(invoice);
        InvoiceDto invoiceDto = new InvoiceDto(invoice, workCosts, invoiceMaterials);
        try {
            byte[] invoice_pdf = pdfService.generateInvoicePdf(invoiceDto);
            emailService.sendInvoice(invoiceDto, invoice_pdf);
        } catch (Exception e) {
            // intentionally silent: keep previous behavior (exception swallowed), but no console output
        }
    }

    public InvoiceDto getInvoice(long invoiceId) {
        long companyId = getCurrentCompanyId();
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceDoesNotExistException("Invoice with this id does not exist"));

        if (!invoice.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("Api user has not the rights to access this invoice");
        }

        List<InvoiceWorkCost> workCosts = invoiceWorkCostRepository.findInvoiceWorkCostByInvoice(invoice);
        List<InvoiceMaterial> invoiceMaterials = invoiceMaterialRepository.findInvoiceMaterialByInvoice(invoice);
        return new InvoiceDto(invoice, workCosts, invoiceMaterials);
    }
}