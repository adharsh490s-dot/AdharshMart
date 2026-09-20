package com.adharsh.adharshmart.dao;

import com.adharsh.adharshmart.listener.DBConnectionListener;
import com.adharsh.adharshmart.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {

    public List<Product> findAll(String category, String keyword) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, seller_id, name, description, price, stock_qty, category, image_url, created_at FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (category != null && !category.isBlank()) {
            sql.append(" AND category = ?");
            params.add(category);
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (LOWER(name) LIKE ? OR LOWER(description) LIKE ?)");
            String wildcard = "%" + keyword.toLowerCase().trim() + "%";
            params.add(wildcard);
            params.add(wildcard);
        }
        sql.append(" ORDER BY id DESC");

        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying products", e);
        }
        return products;
    }

    public Optional<Product> findById(Long id) {
        String sql = "SELECT id, seller_id, name, description, price, stock_qty, category, image_url, created_at FROM products WHERE id = ?";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting product by id", e);
        }
        return Optional.empty();
    }

    public Product save(Product product) {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, product.getSellerId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQty());
            ps.setString(6, product.getCategory());
            ps.setString(7, product.getImageUrl());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) product.setId(rs.getLong(1));
            }
            return product;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating product", e);
        }
    }

    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_qty = ?, category = ?, image_url = ? WHERE id = ? AND seller_id = ?";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStockQty());
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getImageUrl());
            ps.setLong(7, product.getId());
            ps.setLong(8, product.getSellerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public boolean updateStock(Long productId, int stockQty) {
        String sql = "UPDATE products SET stock_qty = ? WHERE id = ?";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stockQty);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating inventory", e);
        }
    }

    public boolean delete(Long id, Long sellerId, boolean isAdmin) {
        String sql = isAdmin ? "DELETE FROM products WHERE id = ?" : "DELETE FROM products WHERE id = ? AND seller_id = ?";
        try (Connection conn = DBConnectionListener.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            if (!isAdmin) ps.setLong(2, sellerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
            rs.getLong("id"),
            rs.getLong("seller_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price"),
            rs.getInt("stock_qty"),
            rs.getString("category"),
            rs.getString("image_url"),
            rs.getTimestamp("created_at")
        );
    }
}