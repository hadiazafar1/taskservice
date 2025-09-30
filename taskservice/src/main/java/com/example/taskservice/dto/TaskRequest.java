package com.example.taskservice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class TaskRequest {

    TaskRequest() {
        // Default constructor for serialization/deserialization
    }
    public TaskRequest(String title, String description, String status, String priority, LocalDateTime dueDate, String assigneeEmail) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.assigneeEmail = assigneeEmail;
    }

    @NotBlank(message = "Title is required")
    public String title;
    public String description;
    @NotBlank(message = "Status is required")
    public String status;
    @NotBlank(message = "Priority is required")
    public String priority;
    @NotNull(message = "Due date is required")
    public LocalDateTime dueDate;
    @NotBlank(message = "Assignee email is required")
    public String assigneeEmail;
}