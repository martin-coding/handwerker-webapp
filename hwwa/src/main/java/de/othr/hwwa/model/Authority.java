package de.othr.hwwa.model;

import java.io.Serializable;
import jakarta.persistence.*;


@Table(name="authority")
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class Authority implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The name of the permission
    @Column(nullable = false, unique = true)
    private String name;

    public Authority() {}

    public Authority(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}