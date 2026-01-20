package de.othr.hwwa.model.dto;

import java.time.LocalDateTime;

public interface ClientTaskCountView {

    Long getId();
    String getName();
    String getEmail();
    LocalDateTime getCreatedAt();

    long getOpenTasks();
    long getCompletedTasks();
    float getTotalAmount();
}
