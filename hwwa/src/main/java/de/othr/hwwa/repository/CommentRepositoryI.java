package de.othr.hwwa.repository;

import de.othr.hwwa.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepositoryI extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskIdOrderByCreatedAtDesc(long taskId);
}