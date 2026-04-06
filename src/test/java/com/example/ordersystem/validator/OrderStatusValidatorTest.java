package com.example.ordersystem.validator;

import com.example.ordersystem.entity.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusValidatorTest {

    @Test
    void shouldAllowValidTransitions() {
        assertTrue(OrderStatusValidator.isValid(OrderStatus.PENDING, OrderStatus.PROCESSING));
        assertTrue(OrderStatusValidator.isValid(OrderStatus.PENDING, OrderStatus.CANCELLED));
        assertTrue(OrderStatusValidator.isValid(OrderStatus.PROCESSING, OrderStatus.SHIPPED));
        assertTrue(OrderStatusValidator.isValid(OrderStatus.SHIPPED, OrderStatus.DELIVERED));
    }

    @Test
    void shouldRejectInvalidTransitions() {
        assertFalse(OrderStatusValidator.isValid(OrderStatus.PENDING, OrderStatus.SHIPPED));
        assertFalse(OrderStatusValidator.isValid(OrderStatus.PROCESSING, OrderStatus.DELIVERED));
        assertFalse(OrderStatusValidator.isValid(OrderStatus.SHIPPED, OrderStatus.PENDING));
    }

    @Test
    void shouldRejectAllTransitionsFromDelivered() {
        assertFalse(OrderStatusValidator.isValid(OrderStatus.DELIVERED, OrderStatus.PENDING));
        assertFalse(OrderStatusValidator.isValid(OrderStatus.DELIVERED, OrderStatus.PROCESSING));
        assertFalse(OrderStatusValidator.isValid(OrderStatus.DELIVERED, OrderStatus.SHIPPED));
        assertFalse(OrderStatusValidator.isValid(OrderStatus.DELIVERED, OrderStatus.CANCELLED));
    }

    @Test
    void shouldRejectAllTransitionsFromCancelled() {
        assertFalse(OrderStatusValidator.isValid(OrderStatus.CANCELLED, OrderStatus.PENDING));
        assertFalse(OrderStatusValidator.isValid(OrderStatus.CANCELLED, OrderStatus.PROCESSING));
        assertFalse(OrderStatusValidator.isValid(OrderStatus.CANCELLED, OrderStatus.SHIPPED));
        assertFalse(OrderStatusValidator.isValid(OrderStatus.CANCELLED, OrderStatus.DELIVERED));
    }
}