// src/main/java/com/adharsh/adharshmart/controller/OrderServlet.java
package com.adharsh.adharshmart.controller;

import com.adharsh.adharshmart.dto.ApiResponse;
import com.adharsh.adharshmart.model.Order;
import com.adharsh.adharshmart.service.OrderService;
import com.adharsh.adharshmart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/orders/*")
public class OrderServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Long buyerId = (Long) req.getSession().getAttribute("userId");
        List<Order> orders = orderService.getBuyerOrders(buyerId);
        resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(orders)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Long buyerId = (Long) req.getSession().getAttribute("userId");
        try {
            Order order = orderService.checkout(buyerId);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(order)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("CHECKOUT_ERROR", e.getMessage())));
        }
    }
}