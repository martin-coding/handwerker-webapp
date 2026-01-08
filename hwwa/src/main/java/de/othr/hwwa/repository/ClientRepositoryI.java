package de.othr.hwwa.repository;

import de.othr.hwwa.model.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepositoryI extends JpaRepository<Client, Long> {
    @Query("""
        SELECT c FROM Client c
        WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Client> search(@Param("keyword") String keyword, Pageable pageable);
}
