package de.othr.hwwa.controller;

import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.service.InvoiceServiceI;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    InvoiceServiceI invoiceService;

    public InvoiceController(InvoiceServiceI invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String showInvoicePage(Model model){
        model.addAttribute("tasks", invoiceService.getAllDoneTasks());
        model.addAttribute("invoices", invoiceService.getAllDoneInvoices());
        return "invoices/invoice_basic";
    }

    @GetMapping("/{taskId}")
    public String getInvoice(@PathVariable int taskId, Model model){
        model.addAttribute("tasks", invoiceService.getAllDoneTasks());
        model.addAttribute("invoices", invoiceService.getAllDoneInvoices());
        model.addAttribute("invoice", invoiceService.getInvoiceDataForTask(taskId));
        return "invoices/invoice";
    }

    @PostMapping("/{taskId}/save")
    public String saveInvoice(@PathVariable int taskId, @Valid @ModelAttribute("invoice") InvoiceDto invoice, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("tasks", invoiceService.getAllDoneTasks());
            model.addAttribute("invoices", invoiceService.getAllDoneInvoices());
            return "invoices/invoice";
        }
        invoiceService.updateInvoice(invoice, taskId);
        return "redirect:/invoices/" + taskId;
    }

    @PostMapping("/{invoiceId}/delete")
    public String deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return "redirect:/invoices";
    }

    @PostMapping("/{invoiceId}/send")
    public String sendInvoice(@PathVariable Long invoiceId){
        invoiceService.sendInvoice(invoiceId);
        return "redirect:/invoices";
    }
}
