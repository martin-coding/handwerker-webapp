package de.othr.hwwa.model.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VacationCreateDto {

    @NotNull
    private Long userId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDateExclusive;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDateExclusive() { return endDateExclusive; }
    public void setEndDateExclusive(LocalDate endDateExclusive) { this.endDateExclusive = endDateExclusive; }
}