package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TaskAssignmentCreateDto {

    @NotNull
    private Long userId;

    @Min(0)
    private int initialMinutes;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public int getInitialMinutes() { return initialMinutes; }
    public void setInitialMinutes(int initialMinutes) { this.initialMinutes = initialMinutes; }
}