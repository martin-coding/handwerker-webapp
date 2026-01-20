package de.othr.hwwa.event;

import java.util.List;

public record AllTodosDoneEvent(
    List<String> recipientEmails,
    String taskTitle
) {}
