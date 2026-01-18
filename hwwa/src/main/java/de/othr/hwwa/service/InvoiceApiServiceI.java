package de.othr.hwwa.service;

import de.othr.hwwa.model.dto.InvoiceDto;

import java.util.List;

public interface InvoiceApiServiceI {
    public List<InvoiceDto> apiGetAllCompanyInvoices(long companyId);
    public InvoiceDto apiGetInvoice(long companyId, long invoiceId);
}
