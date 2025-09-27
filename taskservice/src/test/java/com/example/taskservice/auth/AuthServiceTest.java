package com.example.taskservice.auth;

import com.example.taskservice.model.User;
import com.example.taskservice.dto.RegisterRequest;
import com.example.taskservice.repository.UserProfileRepository;
import com.example.taskservice.repository.UserRepository;
import com.example.taskservice.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    UserRepository userRepository = mock(UserRepository.class);
    UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    JwtService jwtService = mock(JwtService.class);

    AuthService service;

    @BeforeEach
    void setup() {
        service = new AuthService(userRepository, userProfileRepository, passwordEncoder, jwtService);
    }

    @Test
    void register_hashes_password_and_saves_user() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("bob");
        req.setPassword("Passw0rd!");
        req.setFullName("Bob B");
        req.setEmail("b@b.com");
        req.setBio("hi");
        req.setRole("USER");

        when(passwordEncoder.encode("Passw0rd!")).thenReturn("hashed");
        service.registerUser(req);

        ArgumentCaptor<User> userCap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCap.capture());
        assertThat(userCap.getValue().getUsername()).isEqualTo("bob");
        assertThat(userCap.getValue().getPassword()).isEqualTo("hashed");
    }

    @Test
    void login_returns_jwt_on_valid_credentials() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("bob");
        user.setPassword("hashed");

        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Passw0rd!", "hashed")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("TOKEN");

        LoginRequest req = new LoginRequest();
        req.setUsername("bob");
        req.setPassword("Passw0rd!");

        LoginResponse resp = service.login(req);
        assertThat(resp.getToken()).isEqualTo("TOKEN");
    }
}
