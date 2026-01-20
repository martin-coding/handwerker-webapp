package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.Coordinates;
import de.othr.hwwa.model.dto.OpenWeatherResponse;
import de.othr.hwwa.model.dto.WeatherDto;
import de.othr.hwwa.service.WeatherServiceI;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherServiceImpl implements WeatherServiceI {

    private final WebClient webClient;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    public WeatherServiceImpl( ) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openweathermap.org/data/2.5")
                .build();
    }

    @Override
    public WeatherDto getWeather(Coordinates coordinates) {
        Locale locale = LocaleContextHolder.getLocale();
        OpenWeatherResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("lat", coordinates.getLat())
                        .queryParam("lon", coordinates.getLon())
                        .queryParam("appid", weatherApiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", locale.getCountry())
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherResponse.class)
                .block();
        return new WeatherDto(
            response.name(),
            response.weather().get(0).description(),
            response.main().temp(),
            response.wind().speed()
    );
    }
}