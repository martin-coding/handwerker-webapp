package de.othr.hwwa.controller;

import de.othr.hwwa.model.Coordinates;
import de.othr.hwwa.service.TaskServiceI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/tasks")
public class TaskLocationController{

    private final TaskServiceI taskService;

    public TaskLocationController(TaskServiceI taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}/location")
    @ResponseBody
    public Map<String, Double> getLocation(@PathVariable Long taskId) {

        Coordinates c = taskService.getTaskCoordinates(taskId);

        return Map.of(
                "latitude", c.getLat(),
                "longitude", c.getLon()
        );
    }
}
