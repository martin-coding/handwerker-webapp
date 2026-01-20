package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDate;

public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String roleName;
    @DecimalMin(value = "0.0", inclusive = true, message = "Stundenlohn muss mindestens 0 sein")
    private float hourlyRate;
    private LocalDate createdAt;

    public UserDto() {
    }

    public UserDto(String firstName, String lastName, String roleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleName = roleName;
    }

    public UserDto(long id, String firstName, String lastName, String roleName,  float hourlyRate, LocalDate createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleName = roleName;
        this.hourlyRate = hourlyRate;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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

    public String getRoleName() {
        return this.roleName;
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

    public LocalDate getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDate createdAt) {this.createdAt = createdAt;}
}
