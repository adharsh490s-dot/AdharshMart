// src/main/java/com/adharsh/adharshmart/model/CartItem.java
package com.adharsh.adharshmart.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CartItem {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private Timestamp createdAt;

    public CartItem() {}
    public CartItem(Long id, Long userId, Long productId, String productName, BigDecimal productPrice, Integer quantity, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}