package de.othr.hwwa.service;

import de.othr.hwwa.model.dto.HolidayViewDto;

import java.time.LocalDate;
import java.util.List;

public interface HolidayServiceI {
    List<HolidayViewDto> getHolidaysInRange(LocalDate startInclusive, LocalDate endExclusive);
}