package de.othr.hwwa.model.dto;

public record EmployeeWorkTimeDto(
    Long userId,
    String employeeName,
    int totalMinutes
) {}