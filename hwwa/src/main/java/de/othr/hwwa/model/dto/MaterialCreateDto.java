package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaterialCreateDto {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotNull(message = "unitPrice is mandatory")
    private float unitPrice;

    @NotNull(message = "quantity is mandatory")
    private float quantity;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public float getUnitPrice() { return unitPrice; }
    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }

    public float getQuantity() { return quantity; }
    public void setQuantity(float quantity) { this.quantity = quantity; }
}