package de.othr.hwwa.model.dto;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ProfileDto {
    @NotBlank(message = "Vorname darf nicht leer sein")
    @Size(min = 2, max = 50, message = "Vorname muss zwischen 2 und 50 Zeichen lang sein")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ'\\- ]+$", message = "Vorname darf nur Buchstaben, Leerzeichen, Bindestriche oder Apostrophe enthalten")
    private String firstName;

    @NotBlank(message = "Nachname darf nicht leer sein")
    @Size(min = 2, max = 50, message = "Nachname muss zwischen 2 und 50 Zeichen lang sein")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ'\\- ]+$", message = "Nachname darf nur Buchstaben, Leerzeichen, Bindestriche oder Apostrophe enthalten")
    private String lastName;

    @Size(min = 8, max = 100, message = "Passwort muss mindestens 8 Zeichen lang sein")
    private String newPassword;

    @Size(min = 8, max = 100, message = "Passwort muss mindestens 8 Zeichen lang sein")
    private String oldPassword;

    @NotBlank(message = "E-Mail darf nicht leer sein")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben")
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9 /]{7,20}$",
            message = "Ungültige Telefonnummer. Nur Zahlen, Leerzeichen ' ' und '/' erlaubt. Optional + am Anfang."
    )
    private String phoneNumber;
    private boolean changePassword;

    private LocalDate createdAt;


    public ProfileDto() {}



    public ProfileDto(String firstName, String lastName, String email, String phoneNumber, LocalDate createdAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {return oldPassword;}

    public void setOldPassword(String oldPassword) {this.oldPassword = oldPassword;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}

