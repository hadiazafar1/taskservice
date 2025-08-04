package com.example.taskservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class TaskEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public TaskEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishTaskCreated(TaskCreatedEvent event) {
        rabbitTemplate.convertAndSend("task.queue", event);
    }

}
