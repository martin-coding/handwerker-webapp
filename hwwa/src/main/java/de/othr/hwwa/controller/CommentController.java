package de.othr.hwwa.controller;

import de.othr.hwwa.model.Comment;
import de.othr.hwwa.model.Task;
import de.othr.hwwa.model.dto.CommentCreateDto;
import de.othr.hwwa.model.dto.CommentUpdateDto;
import de.othr.hwwa.service.CommentServiceI;
import de.othr.hwwa.service.TaskServiceI;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {

    private final CommentServiceI commentService;
    private final TaskServiceI taskService;

    public CommentController(CommentServiceI commentService, TaskServiceI taskService) {
        this.commentService = commentService;
        this.taskService = taskService;
    }

    @PostMapping("/tasks/{taskId}/comments/add")
    public String addComment(@PathVariable long taskId,
                             @Valid @ModelAttribute("commentCreate") CommentCreateDto dto,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/tasks/" + taskId;
        }

        commentService.createComment(taskId, dto.getText(), dto.getNotificationScope());
        return "redirect:/tasks/" + taskId;
    }

    @GetMapping("/tasks/{taskId}/comments/{id}")
    public String commentDetails(@PathVariable long taskId,
                                 @PathVariable long id,
                                 Model model) {

        Task task = taskService.getAssignedTaskById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found " + taskId));

        Comment comment = commentService.getCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found " + id));

        if (comment.getTask() == null || comment.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Comment does not belong to task");
        }

        CommentUpdateDto updateDto = new CommentUpdateDto();
        updateDto.setText(comment.getText());

        model.addAttribute("task", task);
        model.addAttribute("comment", comment);
        model.addAttribute("commentUpdate", updateDto);
        return "task/comment_details";
    }

    @PostMapping("/tasks/{taskId}/comments/{id}/edit")
    public String editComment(@PathVariable long taskId,
                              @PathVariable long id,
                              @Valid @ModelAttribute("commentUpdate") CommentUpdateDto dto,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/tasks/" + taskId + "/comments/" + id;
        }

        commentService.updateOwnComment(id, dto.getText());
        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/tasks/{taskId}/comments/{id}/delete")
    public String deleteComment(@PathVariable long taskId, @PathVariable long id) {
        commentService.deleteComment(id);
        return "redirect:/tasks/" + taskId;
    }
}