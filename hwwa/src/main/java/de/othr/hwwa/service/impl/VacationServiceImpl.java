package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.Company;
import de.othr.hwwa.model.User;
import de.othr.hwwa.model.Vacation;
import de.othr.hwwa.repository.CompanyRepositoryI;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.repository.VacationRepositoryI;
import de.othr.hwwa.service.VacationServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class VacationServiceImpl implements VacationServiceI {

    private final VacationRepositoryI vacationRepository;
    private final CompanyRepositoryI companyRepository;
    private final UserRepositoryI userRepository;

    public VacationServiceImpl(
            VacationRepositoryI vacationRepository,
            CompanyRepositoryI companyRepository,
            UserRepositoryI userRepository
    ) {
        this.vacationRepository = vacationRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Vacation createVacation(Long companyId, Long userId, LocalDate startDate, LocalDate endDateExclusive) {
        if (companyId == null) throw new IllegalArgumentException("companyId missing");
        if (userId == null) throw new IllegalArgumentException("userId missing");
        if (startDate == null || endDateExclusive == null) throw new IllegalArgumentException("start/end missing");
        if (!startDate.isBefore(endDateExclusive)) throw new IllegalArgumentException("end must be after start");

        Company company = companyRepository.findCompanyById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (user.getCompany() == null || user.getCompany().getId() == null || !user.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("User not in company");
        }

        Vacation v = new Vacation();
        v.setCompany(company);
        v.setUser(user);
        v.setStartDate(startDate);
        v.setEndDateExclusive(endDateExclusive);
        v.setCreatedAt(LocalDateTime.now());

        return vacationRepository.save(v);
    }

    @Override
    public List<Vacation> getVacations(Long companyId, LocalDate startInclusive, LocalDate endExclusive) {
        if (companyId == null || startInclusive == null || endExclusive == null) return List.of();
        if (!startInclusive.isBefore(endExclusive)) return List.of();
        return vacationRepository.findOverlappingForCompany(companyId, startInclusive, endExclusive);
    }

    @Override
    public List<Vacation> getVacationsForUser(Long companyId, Long userId, LocalDate startInclusive, LocalDate endExclusive) {
        if (companyId == null || userId == null || startInclusive == null || endExclusive == null) return List.of();
        if (!startInclusive.isBefore(endExclusive)) return List.of();
        return vacationRepository.findOverlappingForUser(companyId, userId, startInclusive, endExclusive);
    }
}