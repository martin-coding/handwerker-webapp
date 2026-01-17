package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentCreateDto {
    @NotBlank(message = "Kommentar darf nicht leer sein")
    private String text;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}