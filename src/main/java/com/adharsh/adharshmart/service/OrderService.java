package com.adharsh.adharshmart.service;

import com.adharsh.adharshmart.dao.CartDAO;
import com.adharsh.adharshmart.dao.OrderDAO;
import com.adharsh.adharshmart.exception.BadRequestException;
import com.adharsh.adharshmart.model.CartItem;
import com.adharsh.adharshmart.model.Order;

import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;

    public OrderService() {
        this(new OrderDAO(), new CartDAO());
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
    }

    public Order checkout(Long buyerId) {
        List<CartItem> items = cartDAO.findByUserId(buyerId);
        if (items.isEmpty()) {
            throw new BadRequestException("Cart is empty. Add items before checking out.");
        }
        return orderDAO.createOrderFromCart(buyerId, items);
    }

    public List<Order> getBuyerOrders(Long buyerId) {
        return orderDAO.findByBuyerId(buyerId);
    }
}