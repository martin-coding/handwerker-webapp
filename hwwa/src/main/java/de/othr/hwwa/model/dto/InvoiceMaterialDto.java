package de.othr.hwwa.model.dto;

import de.othr.hwwa.model.InvoiceMaterial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class InvoiceMaterialDto {
    @NotBlank(message = "Die Beschreibung darf nicht leer sein")
    @Size(max = 50, message = "Die Beschreibung darf nicht mehr als 50 Zeichen enthalten")
    private String description;

    @Positive(message = "Anzahl muss größer als 0 sein")
    private float count;

    @PositiveOrZero(message = "Der Preis muss mindestens 0€ sein")
    private float pricePerStock;

    @PositiveOrZero(message = "Die Gesamtsumme muss größer gleich 0€ sein")
    private float total;

    public InvoiceMaterialDto() {
    }

    public InvoiceMaterialDto(InvoiceMaterial material) {
        this.description = material.getDescription();
        this.count = material.getCount();
        this.pricePerStock = material.getPricePerStock();
        this.total = material.getTotal();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public float getPricePerStock() {
        return pricePerStock;
    }

    public void setPricePerStock(float pricePerStock) {
        this.pricePerStock = pricePerStock;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
