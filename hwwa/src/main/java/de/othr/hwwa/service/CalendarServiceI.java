package de.othr.hwwa.service;

import de.othr.hwwa.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarServiceI {
    List<Task> getMyAssignedTasksInRange(LocalDateTime startInclusive, LocalDateTime endExclusive);
}