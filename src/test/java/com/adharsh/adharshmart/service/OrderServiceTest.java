package com.adharsh.adharshmart.service;

import com.adharsh.adharshmart.dao.CartDAO;
import com.adharsh.adharshmart.dao.OrderDAO;
import com.adharsh.adharshmart.exception.BadRequestException;
import com.adharsh.adharshmart.model.CartItem;
import com.adharsh.adharshmart.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private CartDAO cartDAO;

    @InjectMocks
    private OrderService orderService;

    private final Long testBuyerId = 101L;

    @Test
    public void testCheckoutWithEmptyCartThrowsBadRequestException() {
        // Given an empty cart
        when(cartDAO.findByUserId(testBuyerId)).thenReturn(Collections.emptyList());

        // When checkout is invoked, expect a BadRequestException before any order creation
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            orderService.checkout(testBuyerId);
        });

        assertEquals("Cart is empty. Add items before checking out.", exception.getMessage());
        // Verify OrderDAO is never called
        verify(orderDAO, never()).createOrderFromCart(anyLong(), anyList());
    }

    @Test
    public void testSuccessfulCheckoutFromCart() {
        // Given a cart with 2 items
        CartItem item1 = new CartItem(1L, testBuyerId, 10L, "Glass Display", new BigDecimal("120.00"), 1, null);
        CartItem item2 = new CartItem(2L, testBuyerId, 11L, "Neon Cable", new BigDecimal("15.00"), 2, null);
        List<CartItem> cartItems = List.of(item1, item2);

        Order expectedOrder = new Order(5001L, testBuyerId, "CONFIRMED", new BigDecimal("150.00"), new Timestamp(System.currentTimeMillis()));

        when(cartDAO.findByUserId(testBuyerId)).thenReturn(cartItems);
        when(orderDAO.createOrderFromCart(eq(testBuyerId), eq(cartItems))).thenReturn(expectedOrder);

        // When checkout is executed
        Order result = orderService.checkout(testBuyerId);

        // Then verify the created order
        assertNotNull(result);
        assertEquals(5001L, result.getId());
        assertEquals("CONFIRMED", result.getStatus());
        assertEquals(new BigDecimal("150.00"), result.getTotalAmount());

        // Verify interactions occurred exactly once
        verify(cartDAO, times(1)).findByUserId(testBuyerId);
        verify(orderDAO, times(1)).createOrderFromCart(testBuyerId, cartItems);
    }

    @Test
    public void testGetBuyerOrdersReturnsList() {
        Order order1 = new Order(1L, testBuyerId, "CONFIRMED", new BigDecimal("49.99"), null);
        Order order2 = new Order(2L, testBuyerId, "DELIVERED", new BigDecimal("199.99"), null);

        when(orderDAO.findByBuyerId(testBuyerId)).thenReturn(List.of(order1, order2));

        List<Order> orders = orderService.getBuyerOrders(testBuyerId);

        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals("DELIVERED", orders.get(1).getStatus());
        verify(orderDAO, times(1)).findByBuyerId(testBuyerId);
    }
}