package com.example.taskservice.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskservice.dto.RegisterRequest;
import com.example.taskservice.model.User;
import com.example.taskservice.model.UserProfile;
import com.example.taskservice.repository.UserProfileRepository;
import com.example.taskservice.repository.UserRepository;
import com.example.taskservice.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, UserProfileRepository userProfileRepository,
            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void registerUser(RegisterRequest registerRequest) {
        // Check if user already exists
        userRepository.findByUsername(registerRequest.getUsername())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Username already exists");
                });

        // Create and save user entity
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "USER"); 

        userRepository.save(user);

        // Create and save user profile
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setFullName(registerRequest.getFullName());
        userProfile.setBio(registerRequest.getBio());

        userProfileRepository.save(userProfile);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }

}
