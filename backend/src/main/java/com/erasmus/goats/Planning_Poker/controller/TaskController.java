// src/main/java/com/erasmus/goats/Planning_Poker/controller/TaskController.java
package com.erasmus.goats.Planning_Poker.controller;

import com.erasmus.goats.Planning_Poker.controller.dto.TaskDto;
import com.erasmus.goats.Planning_Poker.repository.entity.Task;
import com.erasmus.goats.Planning_Poker.repository.entity.UserStory;
import com.erasmus.goats.Planning_Poker.service.TaskService;
import com.erasmus.goats.Planning_Poker.service.UserStoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserStoryService userStoryService;

    public TaskController(TaskService taskService, UserStoryService userStoryService) {
        this.taskService = taskService;
        this.userStoryService = userStoryService;
    }

    @GetMapping("/userstories/{userStoryId}")
    public List<Task> getAll(@PathVariable Long userStoryId) {
        return taskService.findAllByUserStoryId(userStoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskDto taskDto) {
        UserStory userStory = userStoryService.findById(taskDto.getUserStoryId());

        if (userStory == null) {
            return ResponseEntity.badRequest().body("Invalid userStoryId");
        }

        var task = new Task();
        task.setTitle(taskDto.getTitle());           // Set title
        task.setDescription(taskDto.getDescription()); // Set description
        task.setUserStory(userStory);

        taskService.save(task);

        return ResponseEntity.ok(task);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task) {
        Task oldTask = taskService.findById(id);

        if (oldTask != null) {
            task.setId(id);
            taskService.save(task);
            return ResponseEntity.ok(task);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
