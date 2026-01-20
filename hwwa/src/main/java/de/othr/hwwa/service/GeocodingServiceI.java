package de.othr.hwwa.service;

import de.othr.hwwa.model.Address;
import de.othr.hwwa.model.Coordinates;

public interface GeocodingServiceI {

    public Coordinates getOrCreate(Address address);
}
