package com.example.taskservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.model.Task;
import com.example.taskservice.security.JwtAuthenticationFilter;
import com.example.taskservice.service.TaskService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// EXCLUDE your security config + JWT filter from this web slice
@WebMvcTest(
    controllers = TaskController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            classes = {
                com.example.taskservice.config.SecurityConfig.class,
                JwtAuthenticationFilter.class
            })
    }
)
@AutoConfigureMockMvc(addFilters = false) // don't register servlet filters in this slice
class TaskControllerTest {

  @Autowired MockMvc mockMvc;

  // Mock ONLY the controller's dependency
  @MockBean TaskService taskService;

  @Test
  void testCreateTask() throws Exception {
    Task t = new Task();
    t.setId(1L);
    t.setTitle("Write tests");
    when(taskService.createTask(any(TaskRequest.class))).thenReturn(t);

    mockMvc.perform(
            post("/api/tasks/create-task")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title":"Write tests",
                      "description":"desc",
                      "status":"OPEN",
                      "priority":"HIGH",
                      "dueDate":"2030-01-01T10:00:00"
                    }
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void testGetAllTasks() throws Exception {
    Task t = new Task();
    t.setId(1L);
    t.setTitle("A");
    when(taskService.getAllTasks()).thenReturn(List.of(t));

    mockMvc.perform(get("/api/tasks/get-all-tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }
}
