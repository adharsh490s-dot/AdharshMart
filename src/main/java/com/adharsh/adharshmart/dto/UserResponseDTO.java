package com.adharsh.adharshmart.dto;

import java.sql.Timestamp;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private Timestamp createdAt;

    public UserResponseDTO(Long id, String name, String email, String role, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Timestamp getCreatedAt() { return createdAt; }
}