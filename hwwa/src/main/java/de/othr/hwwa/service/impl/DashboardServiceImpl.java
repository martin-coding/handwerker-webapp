package de.othr.hwwa.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.othr.hwwa.model.TaskAssignment;
import de.othr.hwwa.model.TaskStatus;
import de.othr.hwwa.model.dto.EmployeeTaskDto;
import de.othr.hwwa.model.dto.EmployeeWorkTimeDto;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.service.DashboardServiceI;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardServiceI {

    private final TaskAssignmentRepository taskAssignmentRepository;

    public DashboardServiceImpl(TaskAssignmentRepository taskAssignmentRepository) {
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    @Override
    public Page<EmployeeTaskDto> getEmployeeTaskOverview(
            String search,
            Pageable pageable
    ) {

        Page<TaskAssignment> page =
                taskAssignmentRepository.findActiveAssignmentsPaged(
                        List.of(TaskStatus.PLANNED, TaskStatus.IN_PROGRESS),
                        search == null ? "" : search,
                        pageable
                );

        return page.map(ta -> new EmployeeTaskDto(
                ta.getUser().getId(),
                ta.getUser().getFirstName() + " " + ta.getUser().getLastName(),
                ta.getTask().getId(),
                ta.getTask().getTitle(),
                ta.getTask().getStatus()
        ));
    }

    @Override
    public List<EmployeeWorkTimeDto> getEmployeeWorkTimes() {

    LocalDateTime from = LocalDate.now()
            .withDayOfMonth(1)
            .atStartOfDay();

    LocalDateTime to = LocalDateTime.now();

    return taskAssignmentRepository
            .sumMinutesWorkedPerUser(from, to)
            .stream()
            .map(row -> new EmployeeWorkTimeDto(
                    (Long) row[0],
                    row[1] + " " + row[2],
                    ((Long) row[3]).intValue()
            ))
            .toList();
    }
}
