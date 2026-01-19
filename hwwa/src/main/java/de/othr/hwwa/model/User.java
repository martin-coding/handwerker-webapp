package de.othr.hwwa.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="user")
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean twoFactorEnabled = false;

    private String twoFactorSecret;

    private String firstName;

    private String lastName;

    private String password;

    private String email;
    private float hourlyRate;
    private String phoneNumber;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean active = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskAssignment> taskAssignments = new ArrayList<>();

    public List<Task> getAssignedTasks() {
        return taskAssignments.stream().map(TaskAssignment::getTask).toList();
    }

    public void setTaskAssignments(List<TaskAssignment> taskAssignments) {
        this.taskAssignments = taskAssignments;
    }

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;

    public User(){}

    public User(String firstName, String lastName,String email, String password, float hourlyRate, Role role, Company company, LocalDateTime createdAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.hourlyRate = hourlyRate;
        this.role = role;
        this.company = company;
        this.createdAt = createdAt;
    }

    public boolean isTwoFactorEnabled() {return twoFactorEnabled;}

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {this.twoFactorEnabled = twoFactorEnabled;}

    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public Long getId() {
        return id;
    }

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

    public float getHourlyRate() {return hourlyRate;}

    public void setHourlyRate(float hourlyRate) {this.hourlyRate = hourlyRate;}

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getRoleName() {return role.getName();}

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}

    public Company getCompany() {return company;}

    public void setCompany(Company company) {this.company = company;}

    public void setId(Long id) {
        this.id = id;
    }

    public List<TaskAssignment> getTaskAssignments() {
        return taskAssignments;
    }
}