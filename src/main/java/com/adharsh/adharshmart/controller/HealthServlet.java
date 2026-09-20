// src/main/java/com/adharsh/adharshmart/controller/HealthServlet.java
package com.adharsh.adharshmart.controller;

import com.adharsh.adharshmart.listener.DBConnectionListener;
import com.adharsh.adharshmart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

@WebServlet("/api/v1/health")
public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String dbStatus = "DOWN";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection()) {
            if (conn.isValid(1)) dbStatus = "UP";
        } catch (Exception ignored) {}

        resp.getWriter().write(JsonUtil.getGson().toJson(Map.of("status", "UP", "db", dbStatus)));
    }
}