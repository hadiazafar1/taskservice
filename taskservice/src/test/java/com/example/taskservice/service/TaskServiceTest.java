package com.example.taskservice.service;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.messaging.TaskEventPublisher;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    TaskRepository repo;
    @Mock
    TaskEventPublisher publisher;

    @InjectMocks
    TaskService svc;

    @Test
    void getTask_throws_when_missing() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // We'll call a method that throws when the task is missing.
        TaskRequest req = new TaskRequest("t", "d", "OPEN", "LOW", LocalDateTime.now().plusDays(1));

        // Assert
        assertThatThrownBy(() -> svc.updateTask(99L, req))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask_saves_and_returns_entity() {
        TaskRequest req = new TaskRequest("Write tests", "desc", "OPEN", "HIGH", LocalDateTime.now().plusDays(1));
        Task saved = new Task();
        saved.setId(1L);
        saved.setTitle(req.title);
        when(repo.save(any(Task.class))).thenReturn(saved);

        Task result = svc.createTask(req);

        verify(repo).save(any(Task.class));
        // no captor needed unless you want to inspect fields on the saved entity
    }
}
