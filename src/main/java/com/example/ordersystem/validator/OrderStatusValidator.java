
package com.example.ordersystem.validator;

import com.example.ordersystem.entity.OrderStatus;

public class OrderStatusValidator {

    public static boolean isValid(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case PENDING -> next == OrderStatus.PROCESSING || next == OrderStatus.CANCELLED;
            case PROCESSING -> next == OrderStatus.SHIPPED;
            case SHIPPED -> next == OrderStatus.DELIVERED;
            default -> false;
        };
    }
}
