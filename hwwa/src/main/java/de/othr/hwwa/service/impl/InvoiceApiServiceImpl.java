package de.othr.hwwa.service.impl;

import de.othr.hwwa.exceptions.InvoiceDoesNotExistException;
import de.othr.hwwa.model.*;
import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.InvoiceMaterialRepositoryI;
import de.othr.hwwa.repository.InvoiceRepositoryI;
import de.othr.hwwa.repository.InvoiceWorkCostRepositoryI;
import de.othr.hwwa.service.InvoiceApiServiceI;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceApiServiceImpl implements InvoiceApiServiceI {
    private CompanyRepositoryI companyRepository;
    private InvoiceRepositoryI invoiceRepository;
    private InvoiceMaterialRepositoryI invoiceMaterialRepository;
    private InvoiceWorkCostRepositoryI invoiceWorkCostRepository;

    public InvoiceApiServiceImpl(CompanyRepositoryI companyRepository, InvoiceRepositoryI invoiceRepository, InvoiceMaterialRepositoryI invoiceMaterialRepository, InvoiceWorkCostRepositoryI invoiceWorkCostRepository) {
        this.companyRepository = companyRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceMaterialRepository = invoiceMaterialRepository;
        this.invoiceWorkCostRepository = invoiceWorkCostRepository;
    }

    public List<InvoiceDto> apiGetAllCompanyInvoices(long companyId){
        Company company =  companyRepository.getCompanyById(companyId);
        List<Invoice> invoices = invoiceRepository.findByCompany(company);
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceDtos.add(new InvoiceDto(invoice, invoiceWorkCostRepository.findInvoiceWorkCostByInvoice(invoice), invoiceMaterialRepository.findInvoiceMaterialByInvoice(invoice) ));
        }
        return invoiceDtos;
    }

    public InvoiceDto apiGetInvoice(long companyId, long invoiceId){
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            throw new InvoiceDoesNotExistException("Eine Rechnung mit dieser Id existiert nicht");
        }
        if (invoice.getCompany().getId() != companyId) {
            throw new IllegalArgumentException("Sie haben keine Berechtigung auf diese Rechnung zuzugreifen");
        }
        return new InvoiceDto(invoice, invoiceWorkCostRepository.findInvoiceWorkCostByInvoice(invoice), invoiceMaterialRepository.findInvoiceMaterialByInvoice(invoice));
    }
}
