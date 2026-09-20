// src/main/java/com/adharsh/adharshmart/model/Order.java
package com.adharsh.adharshmart.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    private Long id;
    private Long buyerId;
    private String status;
    private BigDecimal totalAmount;
    private Timestamp createdAt;
    private List<OrderItem> items;

    public Order() {}
    public Order(Long id, Long buyerId, String status, BigDecimal totalAmount, Timestamp createdAt) {
        this.id = id;
        this.buyerId = buyerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}