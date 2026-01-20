package de.othr.hwwa.repository;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepositoryI extends JpaRepository<Task, Long> {
    Page<Task> findByCompanyIdAndDeletedFalseAndStatusIn(
            Long companyId,
            Collection<TaskStatus> statuses,
            Pageable pageable
    );

    List<Task> findByTitleContainingIgnoreCaseAndDeletedIsFalse(String title);

    List<Task> findByClientCompanyIdAndDeletedIsFalseOrderByIdAsc(Long companyId);
    Optional<Task> findByIdAndDeletedIsFalse(Long id);
    List<Task> findByCompanyIdAndStatusAndDeletedIsFalse(long companyId, TaskStatus status);
    Long countByStatusAndDeletedFalseAndCompanyId(TaskStatus status, Long companyId);
}