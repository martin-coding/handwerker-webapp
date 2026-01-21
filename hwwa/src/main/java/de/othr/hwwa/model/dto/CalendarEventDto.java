package de.othr.hwwa.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CalendarEventDto(
        String id,
        String title,
        String start,
        String end,
        boolean allDay,
        String url,
        String color
) {}