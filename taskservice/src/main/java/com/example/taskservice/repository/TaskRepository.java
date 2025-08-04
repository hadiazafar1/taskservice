package com.example.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskservice.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
} 

