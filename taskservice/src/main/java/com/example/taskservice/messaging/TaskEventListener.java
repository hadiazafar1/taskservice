package com.example.taskservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TaskEventListener {

    @RabbitListener(queues = "task.queue")
    public void handleTaskCreatedEvent(TaskCreatedEvent event) {
        // Handle the task created event
        System.out.println("Task Created: " + event.title() + " with priority " + event.priority());
        // Additional processing logic can be added here
    }

}
