package de.othr.hwwa.service;

import de.othr.hwwa.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentServiceI {
    List<Comment> getCommentsForTask(long taskId);
    Optional<Comment> getCommentById(long commentId);
    Comment createComment(long taskId, String text);
    Comment updateOwnComment(long commentId, String text);
    void deleteComment(long commentId);
}