package de.othr.hwwa.repository;

import de.othr.hwwa.model.Client;
import de.othr.hwwa.model.dto.ClientTaskCountView;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepositoryI extends JpaRepository<Client, Long> {
    @Query("""
        SELECT 
            c.id AS id,
            c.name AS name,
            c.email AS email,
            c.createdAt AS createdAt,

            SUM(CASE 
                WHEN t.status IN ('PLANNED', 'IN_PROGRESS') THEN 1 
                ELSE 0 
            END) AS openTasks,

            SUM(CASE 
                WHEN t.status IN ('DONE', 'CANCELED') THEN 1 
                ELSE 0 
            END) AS completedTasks,

            COALESCE(SUM(i.totalAmount), 0) AS totalAmount
        FROM Client c
        LEFT JOIN c.tasks t
        LEFT JOIN Invoice i ON i.task = t
        WHERE c.company.id = :companyId
        AND c.active = true
        AND (
            :keyword IS NULL
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        GROUP BY c.id, c.name, c.email, c.createdAt
        """)
    Page<ClientTaskCountView> findClientsWithTaskCounts(
        @Param("companyId") Long companyId,
        @Param("keyword") String keyword,
        Pageable pageable
    );

    List<Client> findByCompanyIdOrderByNameAsc(Long companyId);
}