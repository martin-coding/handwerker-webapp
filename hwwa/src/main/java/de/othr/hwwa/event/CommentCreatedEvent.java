package de.othr.hwwa.event;

import de.othr.hwwa.model.CommentNotificationScope;

public record CommentCreatedEvent(
        Long commentId,
        CommentNotificationScope scope
) {}