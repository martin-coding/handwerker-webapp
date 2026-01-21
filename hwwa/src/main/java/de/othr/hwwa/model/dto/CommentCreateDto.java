package de.othr.hwwa.model.dto;

import de.othr.hwwa.model.CommentNotificationScope;
import jakarta.validation.constraints.NotBlank;

public class CommentCreateDto {

    @NotBlank(message = "Kommentar darf nicht leer sein")
    private String text;

    private CommentNotificationScope notificationScope = CommentNotificationScope.ALL;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public CommentNotificationScope getNotificationScope() { return notificationScope; }
    public void setNotificationScope(CommentNotificationScope notificationScope) { this.notificationScope = notificationScope; }
}