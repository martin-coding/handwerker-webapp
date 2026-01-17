package de.othr.hwwa.service;

import de.othr.hwwa.model.dto.InvoiceDto;

public interface PdfServiceI {
    public byte[] generateInvoicePdf(InvoiceDto invoice) throws Exception;
}
