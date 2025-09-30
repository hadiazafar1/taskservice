package com.example.taskservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.messaging.TaskCreatedEvent;
import com.example.taskservice.messaging.TaskEventPublisher;
import com.example.taskservice.model.Task;
import com.example.taskservice.notify.TaskEmailNotifier;
import com.example.taskservice.repository.TaskRepository;
import static org.assertj.core.api.Assertions.assertThat;
 class TaskServiceTest {

    private final TaskRepository taskRepository=  mock(TaskRepository.class);
    private final TaskEmailNotifier taskEventPublisher=  mock(TaskEmailNotifier.class);
    private final TaskService taskService = new TaskService(taskRepository, taskEventPublisher);

    @Test
    public void testCreateTask() {

        TaskRequest request = new TaskRequest("Test Task", "Description", "OPEN","HIGH" , LocalDateTime.now(),"test@example.com");
        Task task = new Task(1L,request.title, request.description, request.status, request.priority, request.dueDate, request.assigneeEmail);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(request);

        assertThat(createdTask.getId()).isEqualTo(1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskEventPublisher).notifyTaskCreated("test@example.com","1","Test Task");

    }

}
