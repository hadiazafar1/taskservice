package com.example.taskservice.messaging;

public interface MessagingPort {
    void publish(String destination, Object payload);
}
