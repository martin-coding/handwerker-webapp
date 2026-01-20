package de.othr.hwwa.model.dto;

import de.othr.hwwa.model.TaskStatus;

public record EmployeeTaskDto(
    Long userId,
    String employeeName,
    Long taskId,
    String taskTitle,
    TaskStatus status
) {}
