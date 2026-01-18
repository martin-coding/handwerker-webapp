package de.othr.hwwa.controller;

import de.othr.hwwa.config.JwtAuthenticationToken;
import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.service.InvoiceApiServiceI;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceApiController {

    private final InvoiceApiServiceI invoiceApiService;

    public InvoiceApiController(InvoiceApiServiceI invoiceApiService) {
        this.invoiceApiService = invoiceApiService;
    }

    @GetMapping
    public List<InvoiceDto> getInvoices(Authentication authentication) {

        Long companyId =
                ((JwtAuthenticationToken) authentication).getCompanyId();

        return invoiceApiService.apiGetAllCompanyInvoices(companyId);
    }

    @GetMapping("/{invoiceId}")
    public InvoiceDto getInvoice(@PathVariable long invoiceId, Authentication authentication) {

        Long companyId =
                ((JwtAuthenticationToken) authentication).getCompanyId();

        return invoiceApiService.apiGetInvoice(companyId, invoiceId);
    }
}
