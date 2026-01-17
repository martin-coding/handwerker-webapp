package de.othr.hwwa.model;

import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name="material")
@Inheritance(strategy=InheritanceType.JOINED)
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Beschreibung ist notwendig")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Stückpreis darf nicht leer sein")
    @Column(nullable = false)
    private float unitPrice;

    @NotNull(message = "Anzahl muss festgelegt werden")
    @Column(nullable = false)
    private float quantity;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private Task task;

    public Material() {}

    public Long getId() {return id;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public float getQuantity() { return quantity; }

    public void setQuantity(float quantity) { this.quantity = quantity; }

    public float getUnitPrice() { return unitPrice; }

    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }

    public Task getTask() {return task;}

    public void setTask(Task task) {this.task = task;}
}
