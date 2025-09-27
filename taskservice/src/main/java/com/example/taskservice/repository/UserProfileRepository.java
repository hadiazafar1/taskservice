package com.example.taskservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskservice.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}
