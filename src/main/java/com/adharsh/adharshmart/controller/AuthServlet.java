// src/main/java/com/adharsh/adharshmart/controller/AuthServlet.java
package com.adharsh.adharshmart.controller;

import com.adharsh.adharshmart.dto.ApiResponse;
import com.adharsh.adharshmart.dto.UserResponseDTO;
import com.adharsh.adharshmart.service.AuthService;
import com.adharsh.adharshmart.util.JsonUtil;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/v1/auth/*")
public class AuthServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        try {
            if ("/login".equals(path)) {
                JsonObject json = JsonUtil.getGson().fromJson(req.getReader(), JsonObject.class);
                UserResponseDTO user = authService.login(json.get("email").getAsString(), json.get("password").getAsString());

                HttpSession session = req.getSession(false);
                if (session != null) session.invalidate();
                session = req.getSession(true);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userRole", user.getRole());

                resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(user)));
            } else if ("/register".equals(path)) {
                JsonObject json = JsonUtil.getGson().fromJson(req.getReader(), JsonObject.class);
                UserResponseDTO user = authService.register(
                    json.get("name").getAsString(),
                    json.get("email").getAsString(),
                    json.get("password").getAsString(),
                    json.get("role").getAsString()
                );
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(user)));
            } else if ("/logout".equals(path)) {
                HttpSession session = req.getSession(false);
                if (session != null) session.invalidate();
                resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok("Logged out")));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("NOT_FOUND", "Unknown auth route.")));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("AUTH_ERROR", e.getMessage())));
        }
    }
}