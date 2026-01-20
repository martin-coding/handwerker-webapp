package de.othr.hwwa.repository;

import de.othr.hwwa.model.Address;
import de.othr.hwwa.model.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoordinatesRepositoryI extends JpaRepository<Coordinates, Long> {
    Optional<Coordinates> findByAddress(Address address);
}
