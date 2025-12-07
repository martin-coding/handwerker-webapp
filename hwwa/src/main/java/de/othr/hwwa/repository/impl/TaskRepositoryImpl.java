package de.othr.hwwa.repository.impl;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.repository.TaskRepositoryI;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepositoryImpl extends TaskRepositoryI, CrudRepository<Task, Long>{

    List<Task> findByTitleContainingIgnoreCase(String title);


}
