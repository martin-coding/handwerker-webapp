package de.othr.hwwa.model.dto;

public record WeatherDto(
        String city,
        String description,
        double temperature,
        double windSpeed
) {}
