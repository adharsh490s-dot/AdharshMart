package com.adharsh.adharshmart.service;

import com.adharsh.adharshmart.dao.ProductDAO;
import com.adharsh.adharshmart.dto.ProductDTO;
import com.adharsh.adharshmart.exception.BadRequestException;
import com.adharsh.adharshmart.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getProducts(String category, String keyword) {
        return productDAO.findAll(category, keyword);
    }

    public Product createProduct(Long sellerId, ProductDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) throw new BadRequestException("Product name is required.");
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) throw new BadRequestException("Price must be positive.");
        if (dto.getStockQty() == null || dto.getStockQty() < 0) throw new BadRequestException("Stock quantity cannot be negative.");

        Product p = new Product();
        p.setSellerId(sellerId);
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStockQty(dto.getStockQty());
        p.setCategory(dto.getCategory() == null ? "General" : dto.getCategory());
        p.setImageUrl(dto.getImageUrl());

        return productDAO.save(p);
    }

    public void removeProduct(Long productId, Long sellerId, boolean isAdmin) {
        boolean deleted = productDAO.delete(productId, sellerId, isAdmin);
        if (!deleted) throw new BadRequestException("Product not found or permission denied.");
    }
}