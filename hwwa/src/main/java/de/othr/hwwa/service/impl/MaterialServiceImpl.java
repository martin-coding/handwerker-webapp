package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.Material;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.repository.MaterialRepositoryI;
import de.othr.hwwa.repository.TaskAssignmentRepository;
import de.othr.hwwa.repository.TaskRepositoryI;
import de.othr.hwwa.service.MaterialServiceI;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MaterialServiceImpl extends SecurityServiceImpl implements MaterialServiceI {

    private final MaterialRepositoryI materialRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final TaskRepositoryI taskRepository;

    public MaterialServiceImpl(MaterialRepositoryI materialRepository,
                               TaskAssignmentRepository taskAssignmentRepository,
                               TaskRepositoryI taskRepository) {
        this.materialRepository = materialRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.taskRepository = taskRepository;
    }

    private boolean isOwnerOrManager() {
        String roleName = getCurrentUser().getRole() != null ? getCurrentUser().getRole().getName() : null;
        return "Owner".equalsIgnoreCase(roleName) || "Manager".equalsIgnoreCase(roleName);
    }

    private void assertCanAccessTask(long taskId) {
        if (isOwnerOrManager()) {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

            Long currentCompanyId = getCurrentCompany().getId();
            Long taskCompanyId = task.getClient() != null && task.getClient().getCompany() != null
                    ? task.getClient().getCompany().getId()
                    : null;

            if (taskCompanyId == null || !taskCompanyId.equals(currentCompanyId)) {
                throw new AccessDeniedException("Access denied");
            }
            return;
        }

        boolean assigned = taskAssignmentRepository
                .findByUserIdAndTaskId(getCurrentUserId(), taskId)
                .isPresent();

        if (!assigned) {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public List<Material> getMaterialsForTask(long taskId) {
        assertCanAccessTask(taskId);
        return materialRepository.findByTaskIdOrderByIdAsc(taskId);
    }

    @Override
    public Optional<Material> getMaterialById(long id) {
        Optional<Material> materialOpt = materialRepository.findById(id);
        materialOpt.ifPresent(m -> assertCanAccessTask(m.getTask().getId()));
        return materialOpt;
    }

    @Override
    @Transactional
    public Material save(Material material) {
        if (material.getTask() == null) {
            throw new IllegalArgumentException("Material.task must not be null");
        }
        assertCanAccessTask(material.getTask().getId());
        return materialRepository.save(material);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material not found: " + id));

        assertCanAccessTask(material.getTask().getId());
        materialRepository.deleteById(id);
    }
}