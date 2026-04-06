
package com.example.ordersystem.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String error;
}
