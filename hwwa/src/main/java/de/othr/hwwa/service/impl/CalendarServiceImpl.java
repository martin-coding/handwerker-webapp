package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.TaskAssignment;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.service.CalendarServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
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
        if (startInclusive == null || endExclusive == null) {
            return List.of();
        }
        if (!startInclusive.isBefore(endExclusive)) {
            return List.of();
        }

        Long currentCompanyId = getCurrentCompany() != null ? getCurrentCompany().getId() : null;

        return taskAssignmentRepository.findByUserId(getCurrentUserId())
                .stream()
                .map(TaskAssignment::getTask)
                .filter(t -> t != null && t.getStartDateTime() != null && t.getEndDateTime() != null)
                .filter(t -> t.getClient() != null && t.getClient().getCompany() != null)
                .filter(t -> currentCompanyId != null && currentCompanyId.equals(t.getClient().getCompany().getId()))
                .filter(t -> t.getStartDateTime().isBefore(endExclusive) && t.getEndDateTime().isAfter(startInclusive))
                .sorted(Comparator
                        .comparing(Task::getStartDateTime)
                        .thenComparing(t -> t.getId() == null ? 0L : t.getId()))
                .toList();
    }
}