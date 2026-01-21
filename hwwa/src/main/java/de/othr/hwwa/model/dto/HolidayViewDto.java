package de.othr.hwwa.model.dto;

import java.time.LocalDate;

public record HolidayViewDto(
        LocalDate date,
        String label,
        boolean global
) {}