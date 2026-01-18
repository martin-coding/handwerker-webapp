package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.Address;
import de.othr.hwwa.model.Coordinates;
import de.othr.hwwa.model.dto.NominatimResponse;
import de.othr.hwwa.repository.CoordinatesRepositoryI;
import de.othr.hwwa.service.GeocodingServiceI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service
public class GeocodingServiceImpl implements GeocodingServiceI {

    private final CoordinatesRepositoryI coordinatesRepo;
    private final WebClient webClient;

    public GeocodingServiceImpl(CoordinatesRepositoryI coordinatesRepo,   @Value("${mail.username}")String email) {
        this.webClient = WebClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent",
                        "client-location-service/1.0 (" + email + ")")
                .build();
        this.coordinatesRepo = coordinatesRepo;
    }

    public Coordinates getOrCreate(Address address) {
        return coordinatesRepo.findByAddress(address).orElseGet(() -> geocodeAndSave(address));
    }

    private Coordinates geocodeAndSave(Address address) {

        List<NominatimResponse> result = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("street", address.getStreet())
                        .queryParam("city", address.getCity())
                        .queryParam("postalcode", address.getPostalCode())
                        .queryParam("country", address.getCountry())
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToFlux(NominatimResponse.class)
                .collectList()
                .block();

        if (result == null || result.isEmpty()) {
            throw new IllegalStateException("Address not found");
        }

        double lat = Double.parseDouble(result.get(0).getLat());
        double lon = Double.parseDouble(result.get(0).getLon());

        return coordinatesRepo.save(new Coordinates(address, lat, lon));
    }
}
