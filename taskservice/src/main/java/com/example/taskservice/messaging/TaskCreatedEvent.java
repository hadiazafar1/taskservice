package com.example.taskservice.messaging;

import java.time.LocalDateTime;

public record TaskCreatedEvent (
    Long id,
    String title,
    String priority,
    LocalDateTime dueDate)
    {}