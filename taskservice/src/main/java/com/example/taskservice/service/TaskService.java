package com.example.taskservice.service;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.model.Task;
import com.example.taskservice.notify.TaskEmailNotifier;
import com.example.taskservice.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final TaskEmailNotifier notifier;

    public Task createTask(TaskRequest request) {
        Task task = new Task(
                request.title,
                request.description,
                request.status,
                request.priority,
                request.dueDate,
                request.assigneeEmail);
        Task createdTask = repository.save(task);

        if (task.getAssigneeEmail() != null && !task.getAssigneeEmail().isBlank()) {
            notifier.notifyTaskCreated(task.getAssigneeEmail(), task.getId().toString(), task.getTitle());
        }
        return createdTask;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return repository.findById(id);
    }

    public Task updateTask(Long id, TaskRequest request) {

        Task task = repository.findById(id)
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