package de.othr.hwwa.model;

import java.io.Serializable;
import java.util.Objects;

public class TaskAssignmentId implements Serializable {
    private Long userId;
    private Long taskId;

    public TaskAssignmentId() {}

    public TaskAssignmentId(Long userId, Long taskId) {
        this.userId = userId;
        this.taskId = taskId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTaskId() { return taskId; }  // <-- TaskAssignment.task.id
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskAssignmentId that = (TaskAssignmentId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, taskId);
    }
}
