package com.example.taskservice.security;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test") // picks src/test/resources/application-test.properties
class JwtServiceTest {

    @Autowired
    JwtService svc;

    @Test
    void generate_and_validate_token() {
        UserDetails u = User.withUsername("alice").password("x").roles("USER").build();
        String token = svc.generateToken(u);

        assertThat(token).isNotBlank();
        assertThat(svc.extractUsername(token)).isEqualTo("alice");
        assertThat(svc.isTokenValid(token, u)).isTrue();
    }
}