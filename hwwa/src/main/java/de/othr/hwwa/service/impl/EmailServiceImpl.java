package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.EmailDetails;
import de.othr.hwwa.model.User;
import de.othr.hwwa.service.EmailServiceI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailServiceI {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}") private String sender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendRegistrationEmail(User user, String notEncryptedPassword) {
        try {
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up details
            mailMessage.setFrom(sender);
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Herzlich Willkommen - " + user.getCompany().getName());

            String msgBody = String.format(
                    "Hallo " + user.getFirstName() +  ",\n\n" +
                            "dein Benutzerkonto wurde erfolgreich erstellt.\n\n" +
                            "Hier sind deine Zugangsdaten:\n" +
                            "E-Mail: " + user.getEmail() + "\n" +
                            "Passwort: " + notEncryptedPassword + "\n\n" +
                            "Du kannst dich ab sofort hier anmelden:\n" +
                            "http://localhost:8080/login\n\n" +
                            "Bitte ändere das automatisch generierte Passwort nach deiner ersten Anmeldung in der App.\n\n" +
                            "Viele Grüße,\n" +
                            "dein Team,\n" +
                            user.getCompany().getName()
            );

            mailMessage.setText(msgBody);

            // Send the mail
            this.mailSender.send(mailMessage);
            System.out.println("Mail Sent Successfully...");
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            System.out.println("Error while Sending Mail");
        }
    }
}