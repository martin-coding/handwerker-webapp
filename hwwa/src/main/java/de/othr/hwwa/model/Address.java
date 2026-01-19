package de.othr.hwwa.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

@Embeddable
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{address.street.not.blank}")
    @Size(max = 100, message = "{address.street.size}")
    private String street;

    @NotBlank(message = "{address.city.not.blank}")
    @Size(max = 50, message = "{address.city.size}")
    private String city;

    @NotBlank(message = "{address.postalCode.not.blank}")
    @Pattern(
            regexp = "^[A-Za-z0-9 \\-]{3,10}$",
            message = "{address.postalCode.pattern}"
    )
    private String postalCode;

    @NotBlank(message = "{address.country.not.blank}")
    @Pattern(
            regexp = "^[A-Za-zÀ-ÿ]+(?:[ '-][A-Za-zÀ-ÿ]+)*$",
            message = "{address.country.pattern}"
    )
    private String country;

    public Address() {
    }

    public Address(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
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
}
