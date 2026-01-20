package de.othr.hwwa.service;

import java.util.List;

import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.InvoiceDto;

public interface EmailServiceI{
    void sendRegistrationEmail(User user, String notEncryptedPassword);
    void sendTodoNotification(List<String> recipients, String task_title);
    public void sendInvoice(InvoiceDto invoiceDto, byte[] pdf);
}
