package com.adharsh.adharshmart.service;

import com.adharsh.adharshmart.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {
    private final AuthService authService = new AuthService();

    @Test
    public void testInvalidEmailRegistrationThrowsException() {
        assertThrows(BadRequestException.class, () -> {
            authService.register("Name", "invalid-email-string", "password123", "BUYER");
        });
    }

    @Test
    public void testInvalidRoleThrowsException() {
        assertThrows(BadRequestException.class, () -> {
            authService.register("Name", "valid@email.com", "password123", "SUPERADMIN");
        });
    }
}