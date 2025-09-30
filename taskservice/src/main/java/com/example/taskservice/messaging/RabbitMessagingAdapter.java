package com.example.taskservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"local","docker"}) 
public class RabbitMessagingAdapter implements MessagingPort {
  private final RabbitTemplate rabbitTemplate;
  public RabbitMessagingAdapter(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }
  @Override
  public void publish(String destination, Object payload) {
    rabbitTemplate.convertAndSend(destination, payload);
  }
}
