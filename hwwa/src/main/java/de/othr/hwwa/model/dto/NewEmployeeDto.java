package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.*;

public class NewEmployeeDto {
    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 8, max = 100, message = "Passwort muss mindestens 8 Zeichen lang sein")
    private String password;
    @NotBlank
    private String password_check;
    @NotBlank(message = "E-Mail darf nicht leer sein")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben")
    private String email;
    private String roleName;
    @DecimalMin(value = "0.0", inclusive = true, message = "Stundenlohn muss mindestens 0 sein")
    private float hourlyRate;

    public NewEmployeeDto() {
    }

    public NewEmployeeDto(String password, String password_check, String email, String roleName, float hourlyRate) {
        this.password = password;
        this.password_check = password_check;
        this.email = email;
        this.roleName = roleName;
        this.hourlyRate = hourlyRate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_check() {
        return password_check;
    }

    public void setPassword_check(String password_check) {
        this.password_check = password_check;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public float getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(float hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
