package com.adharsh.adharshmart.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQty;
    private String category;
    private String imageUrl;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getStockQty() { return stockQty; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
}