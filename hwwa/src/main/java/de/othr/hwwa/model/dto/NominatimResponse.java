package de.othr.hwwa.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimResponse {

    @JsonProperty("lat")
    private String lat;

    @JsonProperty("lon")
    private String lon;

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
