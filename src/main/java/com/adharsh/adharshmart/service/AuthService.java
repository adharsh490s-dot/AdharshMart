package com.adharsh.adharshmart.service;

import com.adharsh.adharshmart.dao.UserDAO;
import com.adharsh.adharshmart.dto.UserResponseDTO;
import com.adharsh.adharshmart.exception.BadRequestException;
import com.adharsh.adharshmart.exception.UnauthorizedException;
import com.adharsh.adharshmart.model.User;
import com.adharsh.adharshmart.util.PasswordUtil;
import com.adharsh.adharshmart.util.ValidationUtil;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public UserResponseDTO login(String email, String password) {
        if (!ValidationUtil.isValidEmail(email) || !ValidationUtil.isNotEmpty(password)) {
            throw new BadRequestException("Valid email and password are required.");
        }
        User user = userDAO.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!PasswordUtil.verify(password, user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }

    public UserResponseDTO register(String name, String email, String password, String role) {
        if (!ValidationUtil.isNotEmpty(name) || !ValidationUtil.isValidEmail(email) || !ValidationUtil.isNotEmpty(password)) {
            throw new BadRequestException("All fields are mandatory and must be valid.");
        }
        if (!"BUYER".equalsIgnoreCase(role) && !"SELLER".equalsIgnoreCase(role)) {
            throw new BadRequestException("Role must be either BUYER or SELLER.");
        }
        if (userDAO.findByEmail(email).isPresent()) {
            throw new BadRequestException("An account with this email already exists.");
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPasswordHash(PasswordUtil.hash(password));
        newUser.setRole(role.toUpperCase());

        User saved = userDAO.create(newUser);
        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole(), saved.getCreatedAt());
    }
}