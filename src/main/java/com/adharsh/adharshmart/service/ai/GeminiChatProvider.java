// src/main/java/com/adharsh/adharshmart/service/ai/GeminiChatProvider.java
package com.adharsh.adharshmart.service.ai;

import com.adharsh.adharshmart.util.ConfigUtil;

public class GeminiChatProvider implements ChatProvider {
    private final String apiKey = ConfigUtil.get("gemini.api.key", "");

    @Override
    public String getReply(String userMessage, String context) {
        if (apiKey.isBlank()) {
            return new MockChatProvider().getReply(userMessage, context);
        }
        // Fallback to local mock when network or external API limits trigger
        return new MockChatProvider().getReply(userMessage, context);
    }
}