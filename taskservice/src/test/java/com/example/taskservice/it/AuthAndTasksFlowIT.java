package com.example.taskservice.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthAndTasksFlowIT {

  // Postgres container for JPA
  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16-alpine")
          .withDatabaseName("tasksdb")
          .withUsername("postgres")
          .withPassword("postgres");

  // RabbitMQ container (only needed if your app connects to RabbitMQ on startup)
  @Container
  static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.13-alpine");

  @DynamicPropertySource
  static void registerProps(DynamicPropertyRegistry r) {
    // Datasource props
    r.add("spring.datasource.url", postgres::getJdbcUrl);
    r.add("spring.datasource.username", postgres::getUsername);
    r.add("spring.datasource.password", postgres::getPassword);

    // RabbitMQ props (skip if your app no longer requires it)
    r.add("spring.rabbitmq.host", rabbit::getHost);
    r.add("spring.rabbitmq.port", rabbit::getAmqpPort);

    // JWT secret is provided by src/test/resources/application-test.properties
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
