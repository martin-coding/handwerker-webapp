package de.othr.hwwa.repository;

import de.othr.hwwa.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepositoryI extends JpaRepository<Company, Long> {
    Optional<Company> findCompanyById(long id);

    Company getCompanyById(Long id);
}
