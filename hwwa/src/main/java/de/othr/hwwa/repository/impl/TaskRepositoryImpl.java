package de.othr.hwwa.repository.impl;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.repository.TaskRepositoryI;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface TaskRepositoryImpl extends TaskRepositoryI, CrudRepository<Task, Long>{
}
