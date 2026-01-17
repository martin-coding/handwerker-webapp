package de.othr.hwwa.service;

import de.othr.hwwa.model.Material;

import java.util.List;
import java.util.Optional;

public interface MaterialServiceI {
    List<Material> getMaterialsForTask(long taskId);
    Optional<Material> getMaterialById(long id);
    Material save(Material material);
    void deleteById(long id);
}