package de.othr.hwwa.service;

import de.othr.hwwa.model.Coordinates;
import de.othr.hwwa.model.dto.WeatherDto;

public interface WeatherServiceI {
    public WeatherDto getWeather(Coordinates coordinates);
}
