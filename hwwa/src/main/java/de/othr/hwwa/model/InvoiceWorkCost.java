package de.othr.hwwa.model;

import de.othr.hwwa.model.dto.InvoiceWorkCostDto;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="invoice_work_cost")
@Inheritance(strategy= InheritanceType.JOINED)
public class InvoiceWorkCost implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
    private String description;
    private float hoursWorked;
    private float hourlyRate;
    private float total;

    public InvoiceWorkCost() {
    }

    public InvoiceWorkCost(TaskAssignment taskAssignment, Invoice invoice) {
        this.description = taskAssignment.getUser().getFirstName() + ' ' + taskAssignment.getUser().getLastName();
        this.hoursWorked = (float)taskAssignment.getMinutesWorked() / 60;
        this.hourlyRate = taskAssignment.getUser().getHourlyRate();
        this.total = hoursWorked * hourlyRate;
        this.invoice = invoice;
    }

    public InvoiceWorkCost(InvoiceWorkCostDto invoiceWorkCostDto, Invoice invoice){
        this.description = invoiceWorkCostDto.getDescription();
        this.hoursWorked = invoiceWorkCostDto.getHoursWorked();
        this.hourlyRate = invoiceWorkCostDto.getHourlyRate();
        this.total = hoursWorked * hourlyRate;
        this.invoice = invoice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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

