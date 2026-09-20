// src/main/java/com/adharsh/adharshmart/service/ChatService.java
package com.adharsh.adharshmart.service;

import com.adharsh.adharshmart.service.ai.ChatProvider;
import com.adharsh.adharshmart.service.ai.GeminiChatProvider;
import com.adharsh.adharshmart.service.ai.MockChatProvider;
import com.adharsh.adharshmart.util.ConfigUtil;

public class ChatService {
    private final ChatProvider provider;

    public ChatService() {
        String selected = ConfigUtil.get("ai.chatbot.provider", "mock");
        if ("gemini".equalsIgnoreCase(selected)) {
            this.provider = new GeminiChatProvider();
        } else {
            this.provider = new MockChatProvider();
        }
    }

    public String reply(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Please provide a query regarding AdharshMart products or orders.";
        }
        return provider.getReply(userMessage.trim(), "E-commerce Context");
    }
}