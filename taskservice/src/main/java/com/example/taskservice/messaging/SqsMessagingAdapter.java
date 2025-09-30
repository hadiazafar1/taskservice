package com.example.taskservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Profile("aws")
@RequiredArgsConstructor
public class SqsMessagingAdapter implements MessagingPort {

  private static final Logger log = LoggerFactory.getLogger(SqsMessagingAdapter.class);

  private final SqsClient sqs;
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * The full Queue URL (NOT the ARN).
   * Example: https://sqs.eu-west-2.amazonaws.com/123456789012/my-tasks-queue
   */
  @Value("${app.queues.tasks}")
  private String queueUrl;

  @Override
  public void publish(String destinationIgnored, Object payload) {
    try {
      String body = (payload instanceof String)
          ? (String) payload
          : mapper.writeValueAsString(payload);

      sqs.sendMessage(SendMessageRequest.builder()
          .queueUrl(queueUrl)
          .messageBody(body)
          .build());

      log.info("Published message to SQS queueUrl={} size={}", queueUrl, body.length());

    } catch (JsonProcessingException e) {
      // If serialization fails, log clearly (do NOT crash the app)
      log.error("Failed to serialize SQS payload of type {}: {}", 
          payload != null ? payload.getClass().getName() : "null", e.getMessage(), e);
    }
  }
}