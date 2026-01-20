package de.othr.hwwa.repository;

import de.othr.hwwa.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepositoryI extends JpaRepository<Todo, Long> {
    List<Todo> findByTaskIdOrderByDoneAscIdAsc(long taskId);
    boolean existsByTaskIdAndDoneFalse(Long taskId);
}