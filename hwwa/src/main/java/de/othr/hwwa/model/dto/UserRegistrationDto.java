package de.othr.hwwa.model.dto;
import jakarta.validation.constraints.*;

public class UserRegistrationDto {
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
    private String password;
    @NotBlank(message = "E-Mail darf nicht leer sein")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben")
    private String email;

    @NotBlank(message = "Straße darf nicht leer sein")
    @Size(max = 100, message = "Straße darf maximal 100 Zeichen enthalten")
    private String street;
    @NotBlank(message = "Stadt darf nicht leer sein")
    @Size(max = 50, message = "Stadt darf maximal 50 Zeichen enthalten")
    private String city;
    @NotBlank(message = "Postleitzahl darf nicht leer sein")
    @Pattern(
            regexp = "^[A-Za-z0-9 \\-]{3,10}$",
            message = "Ungültige Postleitzahl"
    )
    private String postalCode;
    @NotBlank(message = "Land darf nicht leer sein")
    @Pattern(
            regexp = "^[A-Za-zÀ-ÿ]+(?:[ '-][A-Za-zÀ-ÿ]+)*$",
            message = "Ungültiger Ländername"
    )
    private String country;

    @NotBlank(message = "Firmenname darf nicht leer sein")
    private String companyName;


    public UserRegistrationDto() {}

    public UserRegistrationDto(String firstName, String lastName, String password, String email, String street, String city, String postalCode, String country, String companyName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.companyName = companyName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {return street;}

    public void setStreet(String street) {this.street = street;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getPostalCode() {return postalCode;}

    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}

    public String getCountry() {return country;}

    public void setCountry(String country) {this.country = country;}

    public String getCompanyName() {return companyName;}

    public void setCompanyName(String companyName) {this.companyName = companyName;}
}
