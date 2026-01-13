package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.NotBlank;

public class TwoFactorAuthentificationDto {
    @NotBlank(message="Code darf nicht leer sein")
    private String secret;

    public TwoFactorAuthentificationDto() {
    }

    public TwoFactorAuthentificationDto(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
