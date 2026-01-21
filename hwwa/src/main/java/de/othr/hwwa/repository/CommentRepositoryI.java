package de.othr.hwwa.repository;

import de.othr.hwwa.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryI extends JpaRepository<Comment, Long> {

    List<Comment> findByTaskIdOrderByCreatedAtDesc(long taskId);

    @Query("""
           select c
           from Comment c
           join fetch c.task t
           join fetch c.createdByUser u
           where c.id = :id
           """)
    Optional<Comment> findByIdWithTaskAndUser(@Param("id") Long id);
}