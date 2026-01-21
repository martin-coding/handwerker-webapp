package de.othr.hwwa.repository;

import de.othr.hwwa.model.TaskAssignment;
import de.othr.hwwa.model.TaskAssignmentId;
import de.othr.hwwa.model.TaskStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    // US36 – Aktive Aufgaben pro Mitarbeiter
    @Query("""
        SELECT ta
        FROM TaskAssignment ta
        JOIN FETCH ta.user u
        JOIN FETCH ta.task t
        WHERE u.active = true
          AND t.status IN (:statuses)
        ORDER BY u.lastName, u.firstName
    """)
    List<TaskAssignment> findActiveAssignments(
            @Param("statuses") List<TaskStatus> statuses
    );

    @Query("""
        SELECT ta
        FROM TaskAssignment ta
        JOIN ta.user u
        JOIN ta.task t
        WHERE u.active = true
          AND t.status IN (:statuses)
          AND (
                LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(u.lastName)  LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(t.title)     LIKE LOWER(CONCAT('%', :search, '%'))
          )
    """)
    Page<TaskAssignment> findActiveAssignmentsPaged(
            @Param("statuses") List<TaskStatus> statuses,
            @Param("search") String search,
            Pageable pageable
    );

    // US37 – Arbeitszeiten aggregiert pro Mitarbeiter
    @Query("""
        SELECT u.id, u.firstName, u.lastName, SUM(ta.minutesWorked)
        FROM TaskAssignment ta
        JOIN ta.user u
        WHERE u.active = true
        AND ta.assignedAt BETWEEN :from AND :to
        GROUP BY u.id, u.firstName, u.lastName
    """)
    List<Object[]> sumMinutesWorkedPerUser(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}