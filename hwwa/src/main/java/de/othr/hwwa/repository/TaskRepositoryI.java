package de.othr.hwwa.repository;

import de.othr.hwwa.model.Task;

import java.util.List;

public interface TaskRepositoryI extends MyBaseRepositoryI<Task, Long> {

    List<Task> findByTitleContainingIgnoreCase(String login);

}