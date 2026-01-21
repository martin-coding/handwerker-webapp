package de.othr.hwwa.repository;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(
            value = """
                   select distinct t
                   from Task t
                   join t.client c
                   left join t.taskAssignments ta
                   left join ta.user u
                   where t.deleted = false
                     and c.company.id = :companyId
                     and t.status in (:statuses)
                     and (
                          :keyword is null or :keyword = ''
                          or lower(t.title) like lower(concat('%', :keyword, '%'))
                          or lower(t.description) like lower(concat('%', :keyword, '%'))
                          or lower(c.name) like lower(concat('%', :keyword, '%'))
                          or lower(c.email) like lower(concat('%', :keyword, '%'))
                          or lower(u.firstName) like lower(concat('%', :keyword, '%'))
                          or lower(u.lastName) like lower(concat('%', :keyword, '%'))
                     )
                   """,
            countQuery = """
                   select count(distinct t)
                   from Task t
                   join t.client c
                   left join t.taskAssignments ta
                   left join ta.user u
                   where t.deleted = false
                     and c.company.id = :companyId
                     and t.status in (:statuses)
                     and (
                          :keyword is null or :keyword = ''
                          or lower(t.title) like lower(concat('%', :keyword, '%'))
                          or lower(t.description) like lower(concat('%', :keyword, '%'))
                          or lower(c.name) like lower(concat('%', :keyword, '%'))
                          or lower(c.email) like lower(concat('%', :keyword, '%'))
                          or lower(u.firstName) like lower(concat('%', :keyword, '%'))
                          or lower(u.lastName) like lower(concat('%', :keyword, '%'))
                     )
                   """
    )
    Page<Task> findCompanyTasksPaged(
            @Param("companyId") Long companyId,
            @Param("statuses") Collection<TaskStatus> statuses,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query(
            value = """
                   select distinct t
                   from Task t
                   join t.taskAssignments ta
                   join ta.user u
                   join t.client c
                   where t.deleted = false
                     and c.company.id = :companyId
                     and u.id = :userId
                     and t.status in (:statuses)
                     and (
                          :keyword is null or :keyword = ''
                          or lower(t.title) like lower(concat('%', :keyword, '%'))
                          or lower(t.description) like lower(concat('%', :keyword, '%'))
                          or lower(c.name) like lower(concat('%', :keyword, '%'))
                          or lower(c.email) like lower(concat('%', :keyword, '%'))
                     )
                   """,
            countQuery = """
                   select count(distinct t)
                   from Task t
                   join t.taskAssignments ta
                   join ta.user u
                   join t.client c
                   where t.deleted = false
                     and c.company.id = :companyId
                     and u.id = :userId
                     and t.status in (:statuses)
                     and (
                          :keyword is null or :keyword = ''
                          or lower(t.title) like lower(concat('%', :keyword, '%'))
                          or lower(t.description) like lower(concat('%', :keyword, '%'))
                          or lower(c.name) like lower(concat('%', :keyword, '%'))
                          or lower(c.email) like lower(concat('%', :keyword, '%'))
                     )
                   """
    )
    Page<Task> findAssignedTasksForUserPaged(
            @Param("companyId") Long companyId,
            @Param("userId") Long userId,
            @Param("statuses") Collection<TaskStatus> statuses,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}