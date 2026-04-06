
package com.example.ordersystem.dto;

import com.example.ordersystem.entity.OrderStatus;
import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private OrderStatus status;
    private BigDecimal totalAmount;
}
