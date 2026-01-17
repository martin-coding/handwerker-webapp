package de.othr.hwwa.service;

import de.othr.hwwa.model.Material;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.User;

import java.util.List;

public interface TaskPdfServiceI {
    byte[] buildTaskPdf(User currentUser, Task task, List<Material> materials);
}