// src/main/java/com/adharsh/adharshmart/controller/ChatServlet.java
package com.adharsh.adharshmart.controller;

import com.adharsh.adharshmart.dto.ApiResponse;
import com.adharsh.adharshmart.dto.ChatRequestDTO;
import com.adharsh.adharshmart.service.ChatService;
import com.adharsh.adharshmart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/chat")
public class ChatServlet extends HttpServlet {
    private final ChatService chatService = new ChatService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            ChatRequestDTO dto = JsonUtil.getGson().fromJson(req.getReader(), ChatRequestDTO.class);
            String reply = chatService.reply(dto.getMessage());
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(Map.of("reply", reply))));
        } catch (Exception e) {
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(Map.of("reply", "I am temporarily offline. Please reach out to store support."))));
        }
    }
}