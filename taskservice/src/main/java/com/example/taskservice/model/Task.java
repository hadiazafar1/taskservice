package com.example.taskservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String status; // e.g., PENDING, IN_PROGRESS, DONE

    private String priority; // e.g., LOW, MEDIUM, HIGH

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private String assigneeEmail;

    // Constructors, Getters, Setters
    public Task() {
    }

    public Task(String title, String description, String status, String priority, LocalDateTime dueDate, String assigneeEmail) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.assigneeEmail = assigneeEmail;
    }

    // Getters and setters (or use Lombok if added)

    public Task(long l, String title2, String description2,
            String status2,
            String priority2,
            LocalDateTime dueDate2,
            String assigneeEmail2) {
        //TODO Auto-generated constructor stub
        this.id = l;
        this.title = title2;        
        this.description = description2;
        this.status = status2;
        this.priority = priority2;
        this.dueDate = dueDate2;
        this.assigneeEmail = assigneeEmail2;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }   
    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }
}