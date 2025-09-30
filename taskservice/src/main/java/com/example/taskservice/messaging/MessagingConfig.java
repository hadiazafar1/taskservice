package com.example.taskservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MessagingConfig {
    private static final Logger log = LoggerFactory.getLogger(MessagingConfig.class);

    @Bean
    @Primary
    @ConditionalOnMissingBean(MessagingPort.class)
    public MessagingPort noopMessagingPort() {
        log.warn("No MessagingPort implementation found. Using NO-OP port.");
        return (dest, payload) -> { /* no-op */ };
    }
}
