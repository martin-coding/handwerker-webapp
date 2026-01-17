package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskAssignment;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.service.CalendarServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CalendarServiceImpl extends SecurityServiceImpl implements CalendarServiceI {

    private final TaskAssignmentRepository taskAssignmentRepository;

    public CalendarServiceImpl(TaskAssignmentRepository taskAssignmentRepository) {
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    @Override
    public List<Task> getMyAssignedTasksInRange(LocalDateTime startInclusive, LocalDateTime endExclusive) {
        return taskAssignmentRepository.findByUserId(getCurrentUserId())
                .stream()
                .map(TaskAssignment::getTask)
                .filter(t -> t.getStartDateTime() != null && t.getEndDateTime() != null)
                .filter(t -> t.getStartDateTime().isBefore(endExclusive) && t.getEndDateTime().isAfter(startInclusive))
                .toList();
    }

    @Override
    public List<Task> getMyAssignedTasksWithoutDates() {
        return taskAssignmentRepository.findByUserId(getCurrentUserId())
                .stream()
                .map(TaskAssignment::getTask)
                .filter(t -> t.getStartDateTime() == null || t.getEndDateTime() == null)
                .toList();
    }
}