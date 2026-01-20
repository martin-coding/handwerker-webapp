package de.othr.hwwa.controller;

import de.othr.hwwa.exceptions.InvoiceDoesNotExistException;
import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.service.InvoiceServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceApiController {

    private final InvoiceServiceI invoiceService;

    public InvoiceApiController(InvoiceServiceI invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<InvoiceDto> getInvoices() {
        return invoiceService.getAllDoneInvoices();
    }

    @GetMapping("/{invoiceId}")
    public InvoiceDto getInvoice(@PathVariable long invoiceId) {
        try {
            return invoiceService.getInvoice(invoiceId);
        } catch (InvoiceDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice does not exist");
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no rights to access this invoice");
        }

    }
}
