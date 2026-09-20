package com.adharsh.adharshmart.dao;

import com.adharsh.adharshmart.listener.DBConnectionListener;
import com.adharsh.adharshmart.model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public List<CartItem> findByUserId(Long userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.product_id, p.name, p.price, c.quantity, c.created_at " +
                     "FROM cart_items c JOIN products p ON c.product_id = p.id WHERE c.user_id = ?";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new CartItem(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("product_id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching cart items", e);
        }
        return items;
    }

    public void addItem(Long userId, Long productId, int quantity) {
        String checkSql = "SELECT id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setLong(1, userId);
            checkPs.setLong(2, productId);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    int newQty = rs.getInt("quantity") + quantity;
                    String updateSql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, newQty);
                        updatePs.setLong(2, id);
                        updatePs.executeUpdate();
                    }
                } else {
                    String insertSql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setLong(1, userId);
                        insertPs.setLong(2, productId);
                        insertPs.setInt(3, quantity);
                        insertPs.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding to cart", e);
        }
    }

    public void removeItem(Long userId, Long cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing cart item", e);
        }
    }

    public void clearCart(Long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }
}