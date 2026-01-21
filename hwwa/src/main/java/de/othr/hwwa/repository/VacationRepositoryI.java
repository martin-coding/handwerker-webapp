package de.othr.hwwa.repository;

import de.othr.hwwa.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationRepositoryI extends JpaRepository<Vacation, Long> {

    @Query("""
           select v
           from Vacation v
           where v.company.id = :companyId
             and v.startDate < :endExclusive
             and v.endDateExclusive > :startInclusive
           order by v.startDate asc
           """)
    List<Vacation> findOverlappingForCompany(
            @Param("companyId") Long companyId,
            @Param("startInclusive") LocalDate startInclusive,
            @Param("endExclusive") LocalDate endExclusive
    );

    @Query("""
           select v
           from Vacation v
           where v.company.id = :companyId
             and v.user.id = :userId
             and v.startDate < :endExclusive
             and v.endDateExclusive > :startInclusive
           order by v.startDate asc
           """)
    List<Vacation> findOverlappingForUser(
            @Param("companyId") Long companyId,
            @Param("userId") Long userId,
            @Param("startInclusive") LocalDate startInclusive,
            @Param("endExclusive") LocalDate endExclusive
    );
}