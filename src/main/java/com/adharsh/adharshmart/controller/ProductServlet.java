// src/main/java/com/adharsh/adharshmart/controller/ProductServlet.java
package com.adharsh.adharshmart.controller;

import com.adharsh.adharshmart.dto.ApiResponse;
import com.adharsh.adharshmart.dto.ProductDTO;
import com.adharsh.adharshmart.model.Product;
import com.adharsh.adharshmart.service.ProductService;
import com.adharsh.adharshmart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/products/*")
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String category = req.getParameter("category");
        String search = req.getParameter("search");
        List<Product> products = productService.getProducts(category, search);
        resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(products)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        String role = session == null ? null : (String) session.getAttribute("userRole");
        if (!"SELLER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("FORBIDDEN", "Seller or admin privileges required.")));
            return;
        }

        try {
            ProductDTO dto = JsonUtil.getGson().fromJson(req.getReader(), ProductDTO.class);
            Long sellerId = (Long) session.getAttribute("userId");
            Product created = productService.createProduct(sellerId, dto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(created)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("CREATION_FAILED", e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        String path = req.getPathInfo();
        if (session == null || path == null || path.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("UNAUTHORIZED", "Active session required.")));
            return;
        }
        if (!"ADMIN".equalsIgnoreCase((String) session.getAttribute("userRole"))
                && !"SELLER".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("FORBIDDEN", "Seller or admin privileges required.")));
            return;
        }
        if (path == null || path.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long productId = Long.parseLong(path.substring(1));
        Long userId = (Long) session.getAttribute("userId");
        boolean isAdmin = "ADMIN".equalsIgnoreCase((String) session.getAttribute("userRole"));

        try {
            productService.removeProduct(productId, userId, isAdmin);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok("Product removed.")));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.fail("DELETE_FAILED", e.getMessage())));
        }
    }
}