package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MaterialCreateDto {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotNull(message = "unitPrice is mandatory")
    private BigDecimal unitPrice;

    @NotNull(message = "quantity is mandatory")
    private BigDecimal quantity;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}