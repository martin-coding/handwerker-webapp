package de.othr.hwwa.service.nager;

import java.time.LocalDate;
import java.util.List;

public record NagerHolidayDto(
        LocalDate date,
        String localName,
        boolean global,
        List<String> counties
) {}