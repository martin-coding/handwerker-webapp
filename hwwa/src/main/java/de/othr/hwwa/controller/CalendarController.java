package de.othr.hwwa.controller;

import de.othr.hwwa.service.CalendarServiceI;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Controller
public class CalendarController {

    private final CalendarServiceI calendarService;

    public CalendarController(CalendarServiceI calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/calendar")
    public String calendar(@RequestParam(value = "view", defaultValue = "week") String view,
                           @RequestParam(value = "date", required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           Model model) {

        LocalDate base = (date != null) ? date : LocalDate.now();

        LocalDate rangeStartDate;
        LocalDate rangeEndDateExclusive;

        String v = view == null ? "week" : view.toLowerCase();

        switch (v) {
            case "day" -> {
                rangeStartDate = base;
                rangeEndDateExclusive = base.plusDays(1);
            }
            case "month" -> {
                rangeStartDate = base.withDayOfMonth(1);
                rangeEndDateExclusive = base.with(TemporalAdjusters.firstDayOfNextMonth());
            }
            default -> {
                rangeStartDate = base.with(DayOfWeek.MONDAY);
                rangeEndDateExclusive = rangeStartDate.plusDays(7);
            }
        }

        LocalDateTime start = rangeStartDate.atStartOfDay();
        LocalDateTime endExclusive = rangeEndDateExclusive.atStartOfDay();

        model.addAttribute("view", v);
        model.addAttribute("date", base);
        model.addAttribute("rangeStart", rangeStartDate);
        model.addAttribute("rangeEndExclusive", rangeEndDateExclusive);
        model.addAttribute("initialView", v.equals("day") ? "timeGridDay" : v.equals("month") ? "dayGridMonth" : "timeGridWeek");

        return "calendar/calendar";
    }
}