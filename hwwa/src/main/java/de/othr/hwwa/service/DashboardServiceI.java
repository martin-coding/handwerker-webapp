package de.othr.hwwa.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.othr.hwwa.model.dto.EmployeeTaskDto;
import de.othr.hwwa.model.dto.EmployeeWorkTimeDto;

public interface DashboardServiceI {

    Page<EmployeeTaskDto> getEmployeeTaskOverview(
            String search,
            Pageable pageable
    );

    List<EmployeeWorkTimeDto> getEmployeeWorkTimes();
}
