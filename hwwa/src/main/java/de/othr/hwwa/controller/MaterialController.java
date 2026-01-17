package de.othr.hwwa.controller;

import de.othr.hwwa.model.Material;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.dto.MaterialCreateDto;
import de.othr.hwwa.model.dto.MaterialUpdateDto;
import de.othr.hwwa.service.MaterialServiceI;
import de.othr.hwwa.service.TaskServiceI;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class MaterialController {

    private final MaterialServiceI materialService;
    private final TaskServiceI taskService;

    public MaterialController(MaterialServiceI materialService, TaskServiceI taskService) {
        this.materialService = materialService;
        this.taskService = taskService;
    }

    private Task loadTaskForAccess(long taskId) {
        return taskService.getTaskForMaterialTodoAccess(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
    }

    @GetMapping("/tasks/{taskId}/materials/add")
    public String addMaterialForm(@PathVariable long taskId, Model model) {
        Task task = loadTaskForAccess(taskId);

        model.addAttribute("task", task);
        model.addAttribute("materialCreate", new MaterialCreateDto());
        return "task/material_add";
    }

    @PostMapping("/tasks/{taskId}/materials/add")
    public String addMaterialSubmit(@PathVariable long taskId,
                                    @Valid @ModelAttribute("materialCreate") MaterialCreateDto dto,
                                    BindingResult bindingResult,
                                    Model model) {

        Task task = loadTaskForAccess(taskId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            return "task/material_add";
        }

        Material m = new Material();
        m.setName(dto.getName());
        m.setUnitPrice(dto.getUnitPrice());
        m.setQuantity(dto.getQuantity());
        m.setTask(task);

        Material saved = materialService.save(m);
        return "redirect:/tasks/" + taskId + "/materials/" + saved.getId();
    }

    @GetMapping("/tasks/{taskId}/materials/{materialId}")
    public String materialDetails(@PathVariable long taskId,
                                  @PathVariable long materialId,
                                  Model model) {

        Task task = loadTaskForAccess(taskId);

        Material material = materialService.getMaterialById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material not found: " + materialId));

        if (material.getTask() == null || material.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Material does not belong to task: " + taskId);
        }

        MaterialUpdateDto updateDto = new MaterialUpdateDto();
        updateDto.setName(material.getName());
        updateDto.setUnitPrice(material.getUnitPrice());
        updateDto.setQuantity(material.getQuantity());

        model.addAttribute("task", task);
        model.addAttribute("material", material);
        model.addAttribute("materialUpdate", updateDto);
        return "task/material_details";
    }

    @PostMapping("/tasks/{taskId}/materials/{materialId}/edit")
    public String editMaterial(@PathVariable long taskId,
                               @PathVariable long materialId,
                               @Valid @ModelAttribute("materialUpdate") MaterialUpdateDto dto,
                               BindingResult bindingResult,
                               Model model) {

        Task task = loadTaskForAccess(taskId);

        Material material = materialService.getMaterialById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material not found: " + materialId));

        if (material.getTask() == null || material.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Material does not belong to task: " + taskId);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            model.addAttribute("material", material);
            return "task/material_details";
        }

        material.setName(dto.getName());
        material.setUnitPrice(dto.getUnitPrice());
        material.setQuantity(dto.getQuantity());

        materialService.save(material);
        return "redirect:/tasks/" + taskId + "/materials/" + materialId;
    }

    @PostMapping("/tasks/{taskId}/materials/{materialId}/delete")
    public String deleteMaterial(@PathVariable long taskId, @PathVariable long materialId) {
        Material material = materialService.getMaterialById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material not found: " + materialId));

        if (material.getTask() == null || material.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Material does not belong to task: " + taskId);
        }

        materialService.deleteById(materialId);
        return "redirect:/tasks/" + taskId;
    }
}