package de.othr.hwwa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
@Inheritance(strategy = InheritanceType.JOINED)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "title is mandatory")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "description is mandatory")
    @Column(nullable = false, length = 2000)
    private String description;

    // NEU: Status (wird in tasks.html verwendet)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.PLANNED;

    // NEU: Start/Ende (optional in HTML, aber du wolltest es im Modell)
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    // NEU: Ersteller (wenn du es schon brauchst)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskAssignment> taskAssignments = new ArrayList<>();

    // ---- Convenience ----
    public List<User> getAssignedUsers() {
        return taskAssignments.stream().map(TaskAssignment::getUser).toList();
    }

    // ---- Getter/Setter ----
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public List<TaskAssignment> getTaskAssignments() { return taskAssignments; }
    public void setTaskAssignments(List<TaskAssignment> taskAssignments) { this.taskAssignments = taskAssignments; }

    public static long getSerialversionuid() { return serialVersionUID; }
}