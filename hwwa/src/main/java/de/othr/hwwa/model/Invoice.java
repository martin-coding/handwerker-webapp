package de.othr.hwwa.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="invoice")
@Inheritance(strategy= InheritanceType.JOINED)
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate invoiceCreationDate;
    private LocalDate invoiceIssuedDate;

    // Invoice → Created by User (Many invoices → User created it)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdByUser;

    // Invoice → Task (1–1)
    @OneToOne
    @JoinColumn(name = "task_id", nullable = true, unique = true)
    private Task task;

    // Invoice → Company (Many invoices → One company)
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private String clientName;
    private String clientEmail;
    private Address clientAddress;

    private float totalAmount;

    public Invoice() {
    }

    public Invoice(User createdByUser, Task task, Company company, Client client) {
        this.createdByUser = createdByUser;
        this.task = task;
        this.company = company;
        this.clientName = client.getName();
        this.clientEmail = client.getEmail();
        this.clientAddress = client.getAddress();
        this.invoiceCreationDate = LocalDate.now();
        this.invoiceIssuedDate = LocalDate.now().plusDays(28);
    }

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getClientName() {return clientName;}

    public void setClientName(String clientName) {this.clientName = clientName;}

    public String getClientEmail() {return clientEmail;}

    public void setClientEmail(String clientEmail) {this.clientEmail = clientEmail;}

    public Address getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(Address clientAddress) {
        this.clientAddress = clientAddress;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
