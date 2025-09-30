package com.example.taskservice.messaging;

import org.springframework.stereotype.Component;


@Component
public class TaskEventPublisher {

     private final MessagingPort messaging;

    public TaskEventPublisher(MessagingPort messaging) {
        this.messaging = messaging;
    }

    public void publishTaskCreated(TaskCreatedEvent event) {
        messaging.publish("task.queue", event);
    }

}
