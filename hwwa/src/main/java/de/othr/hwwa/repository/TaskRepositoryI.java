package de.othr.hwwa.repository;

import de.othr.hwwa.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepositoryI extends JpaRepository<Task, Long> {

    List<Task> findByTitleContainingIgnoreCase(String login);

}