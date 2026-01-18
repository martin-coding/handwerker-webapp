package de.othr.hwwa.model.dto;

public record CalendarEventDto(
        String id,
        String title,
        String start,
        String end,
        boolean allDay,
        String url,
        String color
) {}