package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.Comment;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.repository.CommentRepositoryI;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.repository.TaskRepositoryI;
import de.othr.hwwa.service.CommentServiceI;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl extends SecurityServiceImpl implements CommentServiceI {

    private final CommentRepositoryI commentRepository;
    private final TaskRepositoryI taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;

    public CommentServiceImpl(CommentRepositoryI commentRepository,
                              TaskRepositoryI taskRepository,
                              TaskAssignmentRepository taskAssignmentRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    private boolean isOwnerOrManager() {
        String roleName = getCurrentUser().getRole() != null ? getCurrentUser().getRole().getName() : null;
        return "Owner".equalsIgnoreCase(roleName) || "Manager".equalsIgnoreCase(roleName);
    }

    private void assertCanAccessTask(long taskId) {
        if (isOwnerOrManager()) {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
            Long currentCompanyId = getCurrentCompany().getId();
            Long taskCompanyId = task.getClient() != null && task.getClient().getCompany() != null
                    ? task.getClient().getCompany().getId()
                    : null;
            if (taskCompanyId == null || !taskCompanyId.equals(currentCompanyId)) {
                throw new AccessDeniedException("Access denied");
            }
            return;
        }

        boolean assigned = taskAssignmentRepository.findByUserIdAndTaskId(getCurrentUserId(), taskId).isPresent();
        if (!assigned) throw new AccessDeniedException("Access denied");
    }

    @Override
    public List<Comment> getCommentsForTask(long taskId) {
        assertCanAccessTask(taskId);
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
    }

    @Override
    public Optional<Comment> getCommentById(long commentId) {
        Optional<Comment> c = commentRepository.findById(commentId);
        c.ifPresent(x -> assertCanAccessTask(x.getTask().getId()));
        return c;
    }

    @Override
    @Transactional
    public Comment createComment(long taskId, String text) {
        assertCanAccessTask(taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        Comment c = new Comment();
        c.setTask(task);
        c.setText(text);
        c.setCreatedAt(LocalDateTime.now());
        c.setCreatedByUser(getCurrentUser());

        return commentRepository.save(c);
    }

    @Override
    @Transactional
    public Comment updateOwnComment(long commentId, String text) {
        Comment existing = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found " + commentId));

        assertCanAccessTask(existing.getTask().getId());

        if (existing.getCreatedByUser() == null
                || existing.getCreatedByUser().getId() == null
                || !existing.getCreatedByUser().getId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        existing.setText(text);
        return commentRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteComment(long commentId) {
        Comment existing = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found " + commentId));

        assertCanAccessTask(existing.getTask().getId());

        boolean isCreator = existing.getCreatedByUser() != null
                && existing.getCreatedByUser().getId() != null
                && existing.getCreatedByUser().getId().equals(getCurrentUserId());

        if (!isCreator && !isOwnerOrManager()) {
            throw new AccessDeniedException("Access denied");
        }

        commentRepository.deleteById(commentId);
    }

}