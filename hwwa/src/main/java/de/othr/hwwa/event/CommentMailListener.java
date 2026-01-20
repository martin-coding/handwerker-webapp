package de.othr.hwwa.event;

import de.othr.hwwa.model.Comment;
import de.othr.hwwa.model.CommentNotificationScope;
import de.othr.hwwa.model.TaskAssignment;
import de.othr.hwwa.model.User;
import de.othr.hwwa.repository.CommentRepositoryI;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.EmailServiceI;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Objects;

@Component
public class CommentMailListener {

    private final EmailServiceI emailService;
    private final CommentRepositoryI commentRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final UserRepositoryI userRepository;

    public CommentMailListener(
            EmailServiceI emailService,
            CommentRepositoryI commentRepository,
            TaskAssignmentRepository taskAssignmentRepository,
            UserRepositoryI userRepository
    ) {
        this.emailService = emailService;
        this.commentRepository = commentRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.userRepository = userRepository;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommentCreated(CommentCreatedEvent event) {

        if (event.scope() == null || event.scope() == CommentNotificationScope.NONE) return;

        Comment c = commentRepository.findByIdWithTaskAndUser(event.commentId()).orElse(null);
        if (c == null || c.getTask() == null || c.getTask().getId() == null) return;

        Long taskId = c.getTask().getId();
        Long authorId = c.getCreatedByUser() != null ? c.getCreatedByUser().getId() : null;

        Long companyId = c.getCreatedByUser() != null && c.getCreatedByUser().getCompany() != null
                ? c.getCreatedByUser().getCompany().getId()
                : null;
        if (companyId == null) return;

        List<String> recipients = switch (event.scope()) {
            case ALL -> taskAssignmentRepository.findByTaskIdWithUser(taskId).stream()
                    .map(TaskAssignment::getUser)
                    .filter(Objects::nonNull)
                    .filter(u -> u.getEmail() != null && !u.getEmail().isBlank())
                    .filter(u -> authorId == null || u.getId() == null || !u.getId().equals(authorId))
                    .map(u -> u.getEmail().trim())
                    .distinct()
                    .toList();

            case OWNER -> userRepository.findByCompanyIdAndActiveTrueOrderByLastNameAscFirstNameAsc(companyId).stream()
                    .filter(u -> u.getRole() != null && "Owner".equalsIgnoreCase(u.getRole().getName()))
                    .filter(u -> u.getEmail() != null && !u.getEmail().isBlank())
                    .filter(u -> authorId == null || u.getId() == null || !u.getId().equals(authorId))
                    .map(u -> u.getEmail().trim())
                    .distinct()
                    .toList();

            case MANAGER -> userRepository.findByCompanyIdAndActiveTrueOrderByLastNameAscFirstNameAsc(companyId).stream()
                    .filter(u -> u.getRole() != null && "Manager".equalsIgnoreCase(u.getRole().getName()))
                    .filter(u -> u.getEmail() != null && !u.getEmail().isBlank())
                    .filter(u -> authorId == null || u.getId() == null || !u.getId().equals(authorId))
                    .map(User::getEmail)
                    .map(String::trim)
                    .distinct()
                    .toList();

            default -> List.of();
        };

        if (recipients.isEmpty()) return;

        String taskTitle = c.getTask().getTitle() != null ? c.getTask().getTitle() : ("Task " + taskId);
        String link = "http://localhost:8080/tasks/" + taskId;

        emailService.sendCommentNotification(recipients, taskTitle, c.getText(), link);
    }
}