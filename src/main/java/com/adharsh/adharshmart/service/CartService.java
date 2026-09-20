package com.adharsh.adharshmart.service;

import com.adharsh.adharshmart.dao.CartDAO;
import com.adharsh.adharshmart.model.CartItem;

import java.util.List;

public class CartService {
    private final CartDAO cartDAO = new CartDAO();

    public List<CartItem> getCart(Long userId) {
        return cartDAO.findByUserId(userId);
    }

    public void addToCart(Long userId, Long productId, int quantity) {
        cartDAO.addItem(userId, productId, Math.max(quantity, 1));
    }

    public void removeFromCart(Long userId, Long cartItemId) {
        cartDAO.removeItem(userId, cartItemId);
    }
}