// src/main/java/com/adharsh/adharshmart/service/ai/MockChatProvider.java
package com.adharsh.adharshmart.service.ai;

import java.util.Locale;

public class MockChatProvider implements ChatProvider {
    @Override
    public String getReply(String userMessage, String context) {
        String msg = userMessage.toLowerCase(Locale.ROOT);
        if (msg.contains("shipping") || msg.contains("delivery")) {
            return "AdharshMart orders are confirmed instantly and shipped within 2-4 business days.";
        } else if (msg.contains("return") || msg.contains("refund")) {
            return "Items can be returned within 7 days of delivery under our buyer protection guarantee.";
        } else if (msg.contains("pay") || msg.contains("payment")) {
            return "We currently support mock payment confirmation without credit card requirements.";
        } else if (msg.contains("order")) {
            return "You can view your order tracking and history in the Orders navigation section.";
        }
        return "I am your AdharshMart 3D Assistant. How may I help with products, orders, or listings?";
    }
}