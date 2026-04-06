
package com.example.ordersystem.controller;

import com.example.ordersystem.dto.*;
import com.example.ordersystem.entity.*;
import com.example.ordersystem.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest req) {
        log.info("Creating order with {} items", req.getItems().size());
        return new ApiResponse<>(true, service.create(req), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> get(@PathVariable Long id) {
        log.info("fetching order with id {} ", id);
        return new ApiResponse<>(true, service.get(id), null);
    }

    @GetMapping
    public ApiResponse<Page<Order>> list(
            @RequestParam(required = false) OrderStatus status,
            Pageable pageable) {
        log.info("fetching All orders");
        return new ApiResponse<>(true, service.list(status, pageable), null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> cancel(@PathVariable Long id) {
        service.cancel(id);
        return new ApiResponse<>(true, "Cancelled", null);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<?> updateStatus(@PathVariable Long id,
                                       @RequestParam OrderStatus status) {
        service.updateStatus(id, status);
        return new ApiResponse<>(true, "Updated", null);
    }
}
