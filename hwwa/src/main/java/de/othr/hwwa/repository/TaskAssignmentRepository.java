package de.othr.hwwa.repository;

import de.othr.hwwa.model.TaskAssignment;
import de.othr.hwwa.model.TaskAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, TaskAssignmentId> {

    List<TaskAssignment> findByUserId(Long userId);

    List<TaskAssignment> findByTaskId(Long taskId);

    Optional<TaskAssignment> findByUserIdAndTaskId(Long userId, Long taskId);

    @Query("""
           select ta
           from TaskAssignment ta
           join fetch ta.user u
           where ta.task.id = :taskId
           """)
    List<TaskAssignment> findByTaskIdWithUser(@Param("taskId") Long taskId);
}