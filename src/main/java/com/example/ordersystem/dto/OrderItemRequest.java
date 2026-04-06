
package com.example.ordersystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    @NotBlank
    private String productName;
    private int quantity;
    private BigDecimal price;
}
