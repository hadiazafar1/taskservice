package com.example.taskservice.service;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.messaging.TaskCreatedEvent;
import com.example.taskservice.messaging.TaskEventPublisher;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskEventPublisher eventPublisher;

    public TaskService(TaskRepository repository, TaskEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

  
    public Task createTask(TaskRequest request) {
        Task task = new Task(
                request.title,
                request.description,
                request.status,
                request.priority,
                request.dueDate
        );
        Task createdTask=repository.save(task);
    
        eventPublisher.publishTaskCreated(new TaskCreatedEvent(
                createdTask.getId(),
                createdTask.getTitle(),
                createdTask.getPriority(),
                createdTask.getDueDate()
        ));
        return createdTask;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return repository.findById(id);
    }

    public Task updateTask(Long id, TaskRequest request) {

        Task task=repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
            task.setTitle(request.title);
            task.setDescription(request.description);
            task.setStatus(request.status);
            task.setPriority(request.priority);
            task.setDueDate(request.dueDate);
            
        return repository.save(task);
    }

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}