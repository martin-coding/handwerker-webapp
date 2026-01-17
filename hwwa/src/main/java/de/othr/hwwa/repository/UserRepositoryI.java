package de.othr.hwwa.repository;

import java.util.List;
import java.util.Optional;
import de.othr.hwwa.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;

public interface UserRepositoryI extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailIgnoreCase(String email);
    Page<User> findByCompanyIdAndActiveTrue(Long companyId, Pageable pageable);
    @Query("""
        SELECT u FROM User u 
        WHERE u.company.id = :companyId
        AND u.active
        AND (
            :keyword IS NULL OR 
            LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<User> findByCompanyIdAndActiveTrueAndKeyword(
            @Param("companyId") Long companyId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    User getUsersById(Long id);

    List<User> findByCompanyIdAndActiveTrueOrderByLastNameAscFirstNameAsc(Long companyId);
}