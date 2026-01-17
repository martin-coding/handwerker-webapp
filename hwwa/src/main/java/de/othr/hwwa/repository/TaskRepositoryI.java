package de.othr.hwwa.repository;

import de.othr.hwwa.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepositoryI extends JpaRepository<Task, Long> {

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByClientCompanyIdOrderByIdAsc(Long companyId);
}
