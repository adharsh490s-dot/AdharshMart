package com.adharsh.adharshmart.dao;

import com.adharsh.adharshmart.listener.DBConnectionListener;
import com.adharsh.adharshmart.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public List<Review> findByProductId(Long productId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT id, product_id, user_id, rating, comment, created_at FROM reviews WHERE product_id = ? ORDER BY id DESC";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(new Review(
                        rs.getLong("id"),
                        rs.getLong("product_id"),
                        rs.getLong("user_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading reviews", e);
        }
        return reviews;
    }

    public Review create(Review review) {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getProductId());
            ps.setLong(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) review.setId(rs.getLong(1));
            }
            return review;
        } catch (SQLException e) {
            throw new RuntimeException("Error submitting review", e);
        }
    }
}