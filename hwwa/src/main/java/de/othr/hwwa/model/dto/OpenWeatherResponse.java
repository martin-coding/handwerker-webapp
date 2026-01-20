package de.othr.hwwa.model.dto;

import de.othr.hwwa.model.dto.weather.Main;
import de.othr.hwwa.model.dto.weather.Wind;
import de.othr.hwwa.model.dto.weather.Weather;

import java.util.List;

public record OpenWeatherResponse(
        String name,
        Main main,
        Wind wind,
        List<Weather> weather
) {}
