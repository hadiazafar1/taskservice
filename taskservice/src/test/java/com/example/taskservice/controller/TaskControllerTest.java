package com.example.taskservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.model.Task;
import com.example.taskservice.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTask() throws Exception {
        // Create a TaskRequest object and mock the service call
        TaskRequest request = new TaskRequest("Test Task", "Description", "OPEN", "HIGH", LocalDateTime.now());
         Task task = new Task(1L,request.title, request.description, request.status, request.priority, request.dueDate);
        // Mock the service response
        when(taskService.createTask(any(TaskRequest.class))).thenReturn(task);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/tasks/create-task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
    

}
