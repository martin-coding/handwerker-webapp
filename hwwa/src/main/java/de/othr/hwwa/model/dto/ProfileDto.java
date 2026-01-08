package de.othr.hwwa.model.dto;
import jakarta.validation.constraints.*;

public class ProfileDto {
    @NotBlank(message = "Vorname darf nicht leer sein")
    @Size(min = 2, max = 50, message = "Vorname muss zwischen 2 und 50 Zeichen lang sein")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ'\\- ]+$", message = "Vorname darf nur Buchstaben, Leerzeichen, Bindestriche oder Apostrophe enthalten")
    private String firstName;
    @NotBlank(message = "Nachname darf nicht leer sein")
    @Size(min = 2, max = 50, message = "Nachname muss zwischen 2 und 50 Zeichen lang sein")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ'\\- ]+$", message = "Nachname darf nur Buchstaben, Leerzeichen, Bindestriche oder Apostrophe enthalten")
    private String lastName;
    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 8, max = 100, message = "Passwort muss mindestens 8 Zeichen lang sein")
    private String newPassword;
    @NotBlank
    private String oldPassword;
    @NotBlank(message = "E-Mail darf nicht leer sein")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben")
    private String email;
    @NotBlank(message = "Telefonnummer darf nicht leer sein")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Ungültige Telefonnummer. Nur Zahlen, optional + am Anfang"
    )
    private String phoneNumber;


    public ProfileDto() {}



    public ProfileDto(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
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
}

