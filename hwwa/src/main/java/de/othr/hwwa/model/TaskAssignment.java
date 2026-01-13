// TaskAssignment.java
package de.othr.hwwa.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_task")
public class TaskAssignment implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId  // <-- STATT @IdClass
    private TaskAssignmentId id = new TaskAssignmentId();  // <-- neues Feld!

    @MapsId("userId")  // <-- Mappt zur IdClass-Komponente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("taskId")  // <-- Mappt zur IdClass-Komponente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "minutes_worked", nullable = false)
    private int minutesWorked = 0;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    // Konstruktoren
    public TaskAssignment() {}

    public TaskAssignment(User user, Task task) {
        this.id = new TaskAssignmentId(user.getId(), task.getId());
        this.user = user;
        this.task = task;
        this.assignedAt = LocalDateTime.now();
    }

    // Getter/Setter (unverändert)
    public TaskAssignmentId getId() { return id; }
    public void setId(TaskAssignmentId id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) {
        this.user = user;
        if (this.id != null) this.id.setUserId(user.getId());
    }

    public Task getTask() { return task; }
    public void setTask(Task task) {
        this.task = task;
        if (this.id != null) this.id.setTaskId(task.getId());
    }

    public int getMinutesWorked() { return minutesWorked; }
    public void setMinutesWorked(int minutesWorked) {
        if (minutesWorked < 0) throw new IllegalArgumentException("Minutes must be >= 0");
        this.minutesWorked = minutesWorked;
    }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    // equals/hashCode über ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((TaskAssignment) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}