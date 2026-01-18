package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaterialCreateDto {

    @NotBlank(message = "Beschreibung ist notwendig")
    private String description;

    @NotNull(message = "Stückpreis darf nicht leer bleiben")
    private float unitPrice;

    @NotNull(message = "Anzahl muss angeben werden")
    private float quantity;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getUnitPrice() { return unitPrice; }
    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }

    public float getQuantity() { return quantity; }
    public void setQuantity(float quantity) { this.quantity = quantity; }
}