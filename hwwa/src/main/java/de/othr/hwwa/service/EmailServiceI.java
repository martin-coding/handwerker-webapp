package de.othr.hwwa.service;

import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.InvoiceDto;

import java.util.List;

public interface EmailServiceI {
    void sendRegistrationEmail(User user, String notEncryptedPassword);
    void sendInvoice(InvoiceDto invoiceDto, byte[] pdf);
    void sendTodoNotification(List<String> recipients, String task_title);
    void sendCommentNotification(List<String> recipients, String taskTitle, String commentText, String taskLink);
}