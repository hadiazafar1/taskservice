package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.model.Task;
import com.example.taskservice.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-task")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest request) {

        return ResponseEntity.ok(taskService.createTask(request));
    }

    @Operation(summary = "Get all tasks")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(summary = "Get a task by id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("get-task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a task (admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a task (admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-task/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }
}