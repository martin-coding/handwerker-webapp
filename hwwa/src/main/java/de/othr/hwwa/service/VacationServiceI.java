package de.othr.hwwa.service;

import de.othr.hwwa.model.Vacation;

import java.time.LocalDate;
import java.util.List;

public interface VacationServiceI {
    Vacation createVacation(Long companyId, Long userId, LocalDate startDate, LocalDate endDateExclusive);
    List<Vacation> getVacations(Long companyId, LocalDate startInclusive, LocalDate endExclusive);
    List<Vacation> getVacationsForUser(Long companyId, Long userId, LocalDate startInclusive, LocalDate endExclusive);
}