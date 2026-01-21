package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.User;
import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.service.EmailServiceI;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailServiceI {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String normalizeBaseUrl() {
        String base = (baseUrl == null || baseUrl.isBlank()) ? "http://localhost:8080" : baseUrl.trim();
        if (base.endsWith("/")) base = base.substring(0, base.length() - 1);
        return base;
    }

    @Async
    @Override
    public void sendRegistrationEmail(User user, String notEncryptedPassword) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Herzlich Willkommen - " + user.getCompany().getName());

            String loginLink = normalizeBaseUrl() + "/login";

            String msgBody =
                    "Hallo " + user.getFirstName() + ",\n\n" +
                            "dein Benutzerkonto wurde erfolgreich erstellt.\n\n" +
                            "Hier sind deine Zugangsdaten:\n" +
                            "E-Mail: " + user.getEmail() + "\n" +
                            "Passwort: " + notEncryptedPassword + "\n\n" +
                            "Du kannst dich ab sofort hier anmelden:\n" +
                            loginLink + "\n\n" +
                            "Bitte ändere das automatisch generierte Passwort nach deiner ersten Anmeldung in der App.\n\n" +
                            "Viele Grüße,\n" +
                            "dein Team,\n" +
                            user.getCompany().getName();

            mailMessage.setText(msgBody);
            this.mailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("Error while Sending Mail");
        }
    }

    @Async
    @Override
    public void sendInvoice(InvoiceDto invoiceDto, byte[] pdf) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = getMimeMessageHelper(invoiceDto, message);
            helper.addAttachment("Invoice-" + invoiceDto.getId() + ".pdf", new ByteArrayResource(pdf));
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while sending mail with PDF attachment");
        }
    }

    private MimeMessageHelper getMimeMessageHelper(InvoiceDto invoiceDto, MimeMessage message) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender);
        helper.setTo(invoiceDto.getClientEmail());
        helper.setSubject("Invoice");

        String msgBody =
                "Hallo " + invoiceDto.getClientName() + ",\n\n" +
                        "anbei ihre Rechnung als PDF.\n\n" +
                        "Viele Grüße,\nDein Team\n" + invoiceDto.getCompanyName();

        helper.setText(msgBody);
        return helper;
    }

    @Async
    @Override
    public void sendTodoNotification(List<String> recipients, String task_title) {
        for (String user_mail : recipients) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(sender);
                mailMessage.setTo(user_mail);
                mailMessage.setSubject("All TODOs on task completed - " + task_title);

                String emailText =
                        "Hello,\n\n" +
                                "This is a quick notification to let you know that all todos have been completed!\n\n" +
                                "Task name:\n" + task_title + "\n\n" +
                                "Best regards,\n" +
                                "Your Notification Service";

                mailMessage.setText(emailText);
                mailSender.send(mailMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Async
    @Override
    public void sendCommentNotification(List<String> recipients, String taskTitle, String commentText, String taskLink) {
        for (String userMail : recipients) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(sender);
                mailMessage.setTo(userMail);
                mailMessage.setSubject("Neuer Kommentar - " + taskTitle);

                String body =
                        "Hallo,\n\n" +
                                "es wurde ein neuer Kommentar zu einem Task geschrieben, bei dem du beteiligt bist.\n\n" +
                                "Task: " + taskTitle + "\n\n" +
                                "Kommentar:\n" + commentText + "\n\n" +
                                "Link:\n" + taskLink + "\n\n" +
                                "Viele Grüße\n" +
                                "Dein Team";

                mailMessage.setText(body);
                mailSender.send(mailMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}