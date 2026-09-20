// src/main/java/com/adharsh/adharshmart/controller/CartServlet.java
package com.adharsh.adharshmart.controller;

import com.adharsh.adharshmart.dto.ApiResponse;
import com.adharsh.adharshmart.service.CartService;
import com.adharsh.adharshmart.util.JsonUtil;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/v1/cart/*")
public class CartServlet extends HttpServlet {
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Long userId = (Long) req.getSession().getAttribute("userId");
        resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(cartService.getCart(userId))));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Long userId = (Long) req.getSession().getAttribute("userId");
        JsonObject json = JsonUtil.getGson().fromJson(req.getReader(), JsonObject.class);

        Long productId = json.get("productId").getAsLong();
        int quantity = json.has("quantity") ? json.get("quantity").getAsInt() : 1;

        cartService.addToCart(userId, productId, quantity);
        resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok("Item added to cart.")));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Long userId = (Long) req.getSession().getAttribute("userId");
        String path = req.getPathInfo();
        if (path != null && path.length() > 1) {
            Long cartItemId = Long.parseLong(path.substring(1));
            cartService.removeFromCart(userId, cartItemId);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok("Item removed.")));
        }
    }
}