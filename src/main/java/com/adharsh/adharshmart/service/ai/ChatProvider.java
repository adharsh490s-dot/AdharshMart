// src/main/java/com/adharsh/adharshmart/service/ai/ChatProvider.java
package com.adharsh.adharshmart.service.ai;

public interface ChatProvider {
    String getReply(String userMessage, String context);
}