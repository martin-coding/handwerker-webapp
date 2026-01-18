package de.othr.hwwa.controller.api;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.dto.CalendarEventDto;
import de.othr.hwwa.service.CalendarServiceI;
import de.othr.hwwa.service.HolidayServiceI;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarEventsController {

    private final CalendarServiceI calendarService;
    private final HolidayServiceI holidayService;

    public CalendarEventsController(CalendarServiceI calendarService,
                                    HolidayServiceI holidayService) {
        this.calendarService = calendarService;
        this.holidayService = holidayService;
    }

    @GetMapping("/events")
    public List<CalendarEventDto> events(@RequestParam String start,
                                         @RequestParam String end) {

        ParsedRange range = ParsedRange.parse(start, end);

        List<CalendarEventDto> out = new ArrayList<>();

        List<Task> tasks = calendarService.getMyAssignedTasksInRange(range.startDateTimeInclusive(), range.endDateTimeExclusive());
        for (Task t : tasks) {
            out.add(new CalendarEventDto(
                    "task-" + t.getId(),
                    t.getTitle(),
                    t.getStartDateTime().toString(),
                    t.getEndDateTime().toString(),
                    false,
                    "/tasks/" + t.getId(),
                    "#0d6efd"
            ));
        }

        var holidays = holidayService.getHolidaysInRange(range.startDateInclusive(), range.endDateExclusive());
        for (var h : holidays) {
            out.add(new CalendarEventDto(
                    "holiday-" + h.date(),
                    h.label(),
                    h.date().toString(),
                    h.date().plusDays(1).toString(),
                    true,
                    null,
                    "#6c757d"
            ));
        }

        return out;
    }

    private record ParsedRange(
            LocalDateTime startDateTimeInclusive,
            LocalDateTime endDateTimeExclusive,
            LocalDate startDateInclusive,
            LocalDate endDateExclusive
    ) {
        static ParsedRange parse(String start, String end) {
            LocalDateTime sLdt;
            LocalDateTime eLdt;

            try {
                OffsetDateTime s = OffsetDateTime.parse(start);
                OffsetDateTime e = OffsetDateTime.parse(end);
                sLdt = s.toLocalDateTime();
                eLdt = e.toLocalDateTime();
            } catch (Exception ex) {
                try {
                    sLdt = LocalDateTime.parse(start);
                    eLdt = LocalDateTime.parse(end);
                } catch (Exception ex2) {
                    LocalDate today = LocalDate.now();
                    sLdt = today.atStartOfDay();
                    eLdt = today.plusDays(7).atStartOfDay();
                }
            }

            LocalDate sDate = sLdt.toLocalDate();
            LocalDate eDate = eLdt.toLocalDate();

            return new ParsedRange(sLdt, eLdt, sDate, eDate);
        }
    }
}