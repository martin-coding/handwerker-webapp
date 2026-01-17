package de.othr.hwwa.repository;

import de.othr.hwwa.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepositoryI extends JpaRepository<Material, Long> {
    List<Material> findByTaskIdOrderByIdAsc(long taskId);
}
