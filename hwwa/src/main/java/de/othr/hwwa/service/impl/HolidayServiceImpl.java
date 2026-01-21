package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.dto.HolidayViewDto;
import de.othr.hwwa.service.HolidayServiceI;
import de.othr.hwwa.service.nager.HolidayLabeler;
import de.othr.hwwa.service.nager.NagerDateHolidayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class HolidayServiceImpl implements HolidayServiceI {

    private final NagerDateHolidayService nagerDateHolidayService;

    public HolidayServiceImpl(NagerDateHolidayService nagerDateHolidayService) {
        this.nagerDateHolidayService = nagerDateHolidayService;
    }

    @Override
    public List<HolidayViewDto> getHolidaysInRange(LocalDate startInclusive, LocalDate endExclusive) {
        return nagerDateHolidayService.getHolidaysInRange(startInclusive, endExclusive, "DE")
                .stream()
                .map(h -> new HolidayViewDto(h.date(), HolidayLabeler.label(h), h.global()))
                .toList();
    }
}