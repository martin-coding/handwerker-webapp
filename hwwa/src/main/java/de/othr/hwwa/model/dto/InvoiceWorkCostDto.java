package de.othr.hwwa.model.dto;

import de.othr.hwwa.model.InvoiceWorkCost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class InvoiceWorkCostDto {
    @NotBlank(message = "Die Beschreibung darf nicht leer sein")
    @Size(max = 50, message = "Die Beschreibung darf nicht mehr als 50 Zeichen enthalten")
    private String description;

    @Positive(message = "Die gearbeiteten Stunden müssen größer als 0 sein")
    private float hoursWorked;

    @PositiveOrZero(message = "Der Stundensatz muss null oder positiv sein")
    private float hourlyRate;

    @PositiveOrZero(message = "Die Gesamtsumme muss größer gleich 0€ sein")
    private float total;

    public InvoiceWorkCostDto() {
    }

    public InvoiceWorkCostDto(InvoiceWorkCost invoiceWorkCost) {
        this.description = invoiceWorkCost.getDescription();
        this.hoursWorked = invoiceWorkCost.getHoursWorked();
        this.hourlyRate = invoiceWorkCost.getHourlyRate();
        this.total = invoiceWorkCost.getTotal();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(float hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public float getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(float hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
