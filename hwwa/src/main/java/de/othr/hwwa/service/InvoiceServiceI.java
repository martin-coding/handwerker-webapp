package de.othr.hwwa.service;

import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.model.dto.TaskForInvoiceDto;

import java.util.List;

public interface InvoiceServiceI {
    public List<TaskForInvoiceDto> getAllDoneTasks();
    public InvoiceDto getInvoiceDataForTask(long id);
    public void updateInvoice(InvoiceDto invoiceDto, long invoiceId);
    public List<InvoiceDto> getAllDoneInvoices();
    public void deleteInvoice(long invoiceId);
    public void sendInvoice(long invoiceId);
}
