package de.othr.hwwa.repository;

import de.othr.hwwa.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepositoryI extends JpaRepository<Material,Long> {
}
