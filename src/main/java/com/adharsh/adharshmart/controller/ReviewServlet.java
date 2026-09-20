// src/main/java/com/adharsh/adharshmart/controller/ReviewServlet.java
package com.adharsh.adharshmart.controller;

import com.adharsh.adharshmart.dao.ReviewDAO;
import com.adharsh.adharshmart.dto.ApiResponse;
import com.adharsh.adharshmart.model.Review;
import com.adharsh.adharshmart.util.JsonUtil;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/v1/reviews/*")
public class ReviewServlet extends HttpServlet {
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Long productId = Long.parseLong(req.getParameter("productId"));
        resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(reviewDAO.findByProductId(productId))));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        JsonObject json = JsonUtil.getGson().fromJson(req.getReader(), JsonObject.class);
        Review review = new Review();
        review.setUserId((Long) session.getAttribute("userId"));
        review.setProductId(json.get("productId").getAsLong());
        review.setRating(json.get("rating").getAsInt());
        review.setComment(json.get("comment").getAsString());

        Review created = reviewDAO.create(review);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(JsonUtil.getGson().toJson(ApiResponse.ok(created)));
    }
}