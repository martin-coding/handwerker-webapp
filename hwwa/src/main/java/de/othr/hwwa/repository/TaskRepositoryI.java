package de.othr.hwwa.repository;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepositoryI extends JpaRepository<Task, Long> {
    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByClientCompanyIdOrderByIdAsc(Long companyId);
    Optional<Task> findById(Long id);
    List<Task> findByCompanyIdAndStatus(long companyId, TaskStatus status);
}