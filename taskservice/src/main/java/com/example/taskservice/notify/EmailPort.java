package com.example.taskservice.notify;

public interface EmailPort {
    void send(String to, String subject, String htmlBody);
}
