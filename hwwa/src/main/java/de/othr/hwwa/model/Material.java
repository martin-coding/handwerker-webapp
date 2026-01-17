package de.othr.hwwa.model;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="material")
@Inheritance(strategy=InheritanceType.JOINED)
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private int count;
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private Task task;

    public Material() {}

    public Long getId() {return id;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public int getCount() {return count;}

    public void setCount(int count) {this.count = count;}

    public Task getTask() {return task;}

    public void setTask(Task task) {this.task = task;}
}
