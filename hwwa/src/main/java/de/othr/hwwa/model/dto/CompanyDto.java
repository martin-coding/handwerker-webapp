package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CompanyDto {
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

    public CompanyDto() {
    }

    public CompanyDto(String street, String city, String postalCode, String country, String companyName) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.companyName = companyName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
