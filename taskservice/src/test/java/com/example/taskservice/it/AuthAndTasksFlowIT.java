package com.example.taskservice.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc // harmless here; we still use RestTemplate
class AuthAndTasksFlowIT {

  // --- Mock ALL RabbitMQ infrastructure so nothing tries to connect ---
  @MockBean RabbitTemplate rabbitTemplate;
  @MockBean ConnectionFactory rabbitConnectionFactory;
  @MockBean AmqpAdmin amqpAdmin;

  // Postgres container for JPA
  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16-alpine")
          .withDatabaseName("tasksdb")
          .withUsername("postgres")
          .withPassword("postgres");

  @DynamicPropertySource
  static void registerProps(DynamicPropertyRegistry r) {
    // Datasource props from Testcontainers Postgres
    r.add("spring.datasource.url", postgres::getJdbcUrl);
    r.add("spring.datasource.username", postgres::getUsername);
    r.add("spring.datasource.password", postgres::getPassword);

    // Hard-disable Rabbit auto-config in this test context
    r.add("spring.autoconfigure.exclude",
        () -> "org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration");

    // Extra safety: ensure any listeners/templates won't auto-start
    r.add("spring.rabbitmq.listener.simple.auto-startup", () -> "false");
    r.add("spring.rabbitmq.listener.direct.auto-startup", () -> "false");
    r.add("spring.rabbitmq.publisher-confirm-type", () -> "none");
    r.add("spring.rabbitmq.publisher-returns", () -> "false");
  }

  @LocalServerPort
  int port;

  @Autowired
  ObjectMapper om;

  private final RestTemplate http = new RestTemplate();

  private String base() {
    return "http://localhost:" + port;
  }

  @Test
  void full_auth_flow_and_secured_endpoint() throws Exception {
    // 1) Register (public)
    String regBody = """
        {
          "username":"admin",
          "password":"Passw0rd!",
          "fullName":"Admin",
          "email":"admin@example.com",
          "bio":"hi",
          "role":"ADMIN"
        }
        """;

    ResponseEntity<String> regResp =
        http.exchange(
            URI.create(base() + "/api/auth/register"),
            HttpMethod.POST,
            new HttpEntity<>(regBody, jsonHeaders()),
            String.class);

    assertThat(regResp.getStatusCode().is2xxSuccessful()).isTrue();

    // 2) Login (public) -> get token
    String loginBody = """
        { "username":"admin", "password":"Passw0rd!" }
        """;

    ResponseEntity<String> loginResp =
        http.exchange(
            URI.create(base() + "/api/auth/login"),
            HttpMethod.POST,
            new HttpEntity<>(loginBody, jsonHeaders()),
            String.class);

    assertThat(loginResp.getStatusCode().is2xxSuccessful()).isTrue();

    JsonNode node = om.readTree(loginResp.getBody());
    String token = node.get("token").asText();
    assertThat(token).isNotBlank();

    // 3) Call secured endpoint with Bearer token
    HttpHeaders headers = jsonHeaders();
    headers.setBearerAuth(token);

    ResponseEntity<String> tasksResp =
        http.exchange(
            URI.create(base() + "/api/tasks/get-all-tasks"),
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class);

    assertThat(tasksResp.getStatusCode().is2xxSuccessful()).isTrue();
  }

  private HttpHeaders jsonHeaders() {
    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_JSON);
    return h;
  }
}
