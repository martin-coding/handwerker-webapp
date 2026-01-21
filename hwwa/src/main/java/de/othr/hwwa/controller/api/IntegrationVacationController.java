package de.othr.hwwa.controller.api;

import de.othr.hwwa.config.JwtAuthenticationToken;
import de.othr.hwwa.model.Vacation;
import de.othr.hwwa.model.dto.VacationCreateDto;
import de.othr.hwwa.service.VacationServiceI;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/integrations/vacations")
public class IntegrationVacationController {

    private final VacationServiceI vacationService;

    public IntegrationVacationController(VacationServiceI vacationService) {
        this.vacationService = vacationService;
    }

    private Long getCompanyIdFromJwt() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a instanceof JwtAuthenticationToken jwt) {
            return jwt.getCompanyId();
        }
        throw new AccessDeniedException("No company scope");
    }

    @GetMapping
    public List<Vacation> list(@RequestParam LocalDate start,
                               @RequestParam LocalDate end) {
        return vacationService.getVacations(getCompanyIdFromJwt(), start, end);
    }

    @PostMapping
    public Vacation create(@Valid @RequestBody VacationCreateDto dto) {
        return vacationService.createVacation(
                getCompanyIdFromJwt(),
                dto.getUserId(),
                dto.getStartDate(),
                dto.getEndDateExclusive()
        );
    }
}