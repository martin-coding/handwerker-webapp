package de.othr.hwwa.model.dto;

import de.othr.hwwa.model.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDto {
    private long id;
    private long taskId;
    private LocalDate invoiceCreationDate;
    private LocalDate invoiceIssuedDate;
    private String taskReference;
    private String createdByEmail;
    private String companyName;
    private Address companyAddress;
    private String clientName;
    private String clientEmail;
    private Address clientAddress;
    @Valid
    private List<InvoiceMaterialDto> materials = new ArrayList<InvoiceMaterialDto>();
    @Valid
    private List<InvoiceWorkCostDto> workCosts =  new ArrayList<InvoiceWorkCostDto>();
    private float total;

    public InvoiceDto() {
    }

    public InvoiceDto(Invoice invoice, List<InvoiceWorkCost> workCosts, List<InvoiceMaterial> materials) {
        this.taskReference = "#" + invoice.getTask().getId() + " " + invoice.getTask().getTitle();
        this.id = invoice.getId();
        this.taskId = invoice.getTask().getId();
        this.invoiceCreationDate = LocalDate.now();
        this.invoiceIssuedDate = LocalDate.now().plusDays(28);
        this.createdByEmail = invoice.getCreatedByUser().getEmail();
        this.companyName = invoice.getCompany().getName();
        this.companyAddress = invoice.getCompany().getAddress();
        this.clientName = invoice.getClient().getName();
        this.clientEmail = invoice.getClient().getEmail();
        this.clientAddress = invoice.getClient().getAddress();
        for (InvoiceMaterial material : materials) {
            this.materials.add(new InvoiceMaterialDto(material));
            this.total += material.getTotal();
        }
        for (InvoiceWorkCost workCost : workCosts) {
            this.workCosts.add(new InvoiceWorkCostDto(workCost));
            this.total += workCost.getTotal();
        }
    }

    public String getTaskReference() {
        return taskReference;
    }

    public void setTaskReference(String taskReference) {
        this.taskReference = taskReference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public LocalDate getInvoiceCreationDate() {
        return invoiceCreationDate;
    }

    public void setInvoiceCreationDate(LocalDate invoiceCreationDate) {
        this.invoiceCreationDate = invoiceCreationDate;
    }

    public LocalDate getInvoiceIssuedDate() {
        return invoiceIssuedDate;
    }

    public void setInvoiceIssuedDate(LocalDate invoiceIssuedDate) {
        this.invoiceIssuedDate = invoiceIssuedDate;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Address getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(Address companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public Address getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(Address clientAddress) {
        this.clientAddress = clientAddress;
    }

    public List<InvoiceMaterialDto> getMaterials() {
        return materials;
    }

    public void setMaterials(List<InvoiceMaterialDto> materials) {
        this.materials = materials;
    }

    public List<InvoiceWorkCostDto> getWorkCosts() {
        return workCosts;
    }

    public void setWorkCosts(List<InvoiceWorkCostDto> workCosts) {
        this.workCosts = workCosts;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
