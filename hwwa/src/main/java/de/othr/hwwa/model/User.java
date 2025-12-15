package de.othr.hwwa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="user")
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean active = true;

    @ManyToMany
    @JoinTable(
            name="user_task",
            joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="task_id", referencedColumnName = "id")
    )
    private List<Task> tasks = new ArrayList<>();

    public User(){}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public Long getId() {
        return id;
    }

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}
}