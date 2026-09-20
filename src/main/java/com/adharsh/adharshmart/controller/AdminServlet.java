// src/main/java/com/adharsh/adharshmart/controller/AdminServlet.java
package com.adharsh.adharshmart.controller;

import com.adharsh.adharshmart.dao.OrderDAO;
import com.adharsh.adharshmart.dao.ProductDAO;
import com.adharsh.adharshmart.dao.UserDAO;
import com.adharsh.adharshmart.dto.ApiResponse;
import com.adharsh.adharshmart.util.JsonUtil;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/admin/*")
public class AdminServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session == null || !"ADMIN".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("FORBIDDEN", "Admin privileges required.")));
            return;
        }

        resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(Map.of(
            "users", userDAO.findAll(),
            "orders", orderDAO.findAll(),
            "products", productDAO.findAll(null, null)
        ))));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session == null || !"ADMIN".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("FORBIDDEN", "Admin privileges required.")));
            return;
        }

        String path = req.getPathInfo();
        if (path == null || !path.startsWith("/inventory/")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("NOT_FOUND", "Use /inventory/{productId}.")));
            return;
        }

        try {
            long productId = Long.parseLong(path.substring("/inventory/".length()));
            JsonObject json = JsonUtil.getGson().fromJson(req.getReader(), JsonObject.class);
            int stockQty = json.get("stockQty").getAsInt();
            if (stockQty < 0 || !productDAO.updateStock(productId, stockQty)) {
                throw new IllegalArgumentException("Product not found or stock quantity is invalid.");
            }
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(productDAO.findById(productId).orElseThrow())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("INVENTORY_UPDATE_FAILED", e.getMessage())));
        }
    }
}