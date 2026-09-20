package com.adharsh.adharshmart.dao;

import com.adharsh.adharshmart.listener.DBConnectionListener;
import com.adharsh.adharshmart.model.CartItem;
import com.adharsh.adharshmart.model.Order;
import com.adharsh.adharshmart.model.OrderItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public Order createOrderFromCart(Long buyerId, List<CartItem> cartItems) {
        Connection conn = null;
        try {
            conn = DBConnectionListener.getDataSource().getConnection();
            conn.setAutoCommit(false);

            BigDecimal total = BigDecimal.ZERO;
            for (CartItem item : cartItems) {
                total = total.add(item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            long orderId;
            String insertOrder = "INSERT INTO orders (buyer_id, status, total_amount) VALUES (?, 'CONFIRMED', ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, buyerId);
                ps.setBigDecimal(2, total);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getLong(1);
                    } else {
                        throw new SQLException("Failed to retrieve generated order id");
                    }
                }
            }

            String insertItem = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            String updateStock = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
            try (PreparedStatement itemPs = conn.prepareStatement(insertItem);
                 PreparedStatement stockPs = conn.prepareStatement(updateStock)) {
                for (CartItem ci : cartItems) {
                    itemPs.setLong(1, orderId);
                    itemPs.setLong(2, ci.getProductId());
                    itemPs.setInt(3, ci.getQuantity());
                    itemPs.setBigDecimal(4, ci.getProductPrice());
                    itemPs.addBatch();

                    stockPs.setInt(1, ci.getQuantity());
                    stockPs.setLong(2, ci.getProductId());
                    stockPs.setInt(3, ci.getQuantity());
                    int updated = stockPs.executeUpdate();
                    if (updated == 0) {
                        throw new SQLException("Insufficient stock for product ID: " + ci.getProductId());
                    }
                }
                itemPs.executeBatch();
            }

            String clearCart = "DELETE FROM cart_items WHERE user_id = ?";
            try (PreparedStatement clearPs = conn.prepareStatement(clearCart)) {
                clearPs.setLong(1, buyerId);
                clearPs.executeUpdate();
            }

            conn.commit();
            return new Order(orderId, buyerId, "CONFIRMED", total, new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public List<Order> findByBuyerId(Long buyerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, buyer_id, status, total_amount, created_at FROM orders WHERE buyer_id = ? ORDER BY id DESC";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                        rs.getLong("id"),
                        rs.getLong("buyer_id"),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching buyer orders", e);
        }
        return orders;
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, buyer_id, status, total_amount, created_at FROM orders ORDER BY id DESC";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(new Order(
                    rs.getLong("id"),
                    rs.getLong("buyer_id"),
                    rs.getString("status"),
                    rs.getBigDecimal("total_amount"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all orders", e);
        }
        return orders;
    }
}