package de.othr.hwwa.service;

import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.InvoiceDto;

public interface EmailServiceI{
    void sendRegistrationEmail(User user, String notEncryptedPassword);
    public void sendInvoice(InvoiceDto invoiceDto, byte[] pdf);
}
