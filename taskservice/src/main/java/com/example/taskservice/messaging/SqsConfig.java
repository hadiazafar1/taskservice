package com.example.taskservice.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * Creates a real SqsClient ONLY when the 'aws' profile is active.
 * DefaultCredentialsProvider looks for creds in this order:
 *  - Env vars (AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY / AWS_SESSION_TOKEN)
 *  - AWS CLI profile (C:\Users\<you>\.aws\credentials)
 *  - EC2/ECS role when running in AWS
 */

@Configuration
@Profile("aws")
public class SqsConfig {

  @Bean
  public SqsClient sqsClient() {
    return SqsClient.builder()
        // Pick your region; London is eu-west-2
        .region(Region.EU_WEST_2)
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
  }
}