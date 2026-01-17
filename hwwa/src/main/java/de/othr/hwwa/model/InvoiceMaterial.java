package de.othr.hwwa.model;

import de.othr.hwwa.model.dto.InvoiceMaterialDto;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="invoice_material")
@Inheritance(strategy= InheritanceType.JOINED)
public class InvoiceMaterial implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
    private String description;
    private float count;
    private float pricePerStock;
    private float total;

    public InvoiceMaterial() {
    }

    public InvoiceMaterial(Material material, Invoice invoice) {
        this.description = material.getDescription();
        this.count = material.getCount();
        this.total = this.pricePerStock * this.count;
        this.invoice = invoice;
    }

    public InvoiceMaterial(InvoiceMaterialDto invoiceMaterialDto, Invoice invoice){
        this.description = invoiceMaterialDto.getDescription();
        this.count = invoiceMaterialDto.getCount();
        this.pricePerStock = invoiceMaterialDto.getPricePerStock();
        this.total = this.pricePerStock * this.count;
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
