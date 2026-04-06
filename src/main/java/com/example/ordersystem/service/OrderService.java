
package com.example.ordersystem.service;

import com.example.ordersystem.dto.*;
import com.example.ordersystem.entity.*;
import com.example.ordersystem.exception.InvalidOrderStateException;
import com.example.ordersystem.exception.OrderNotFoundException;
import com.example.ordersystem.repository.OrderRepository;
import com.example.ordersystem.validator.OrderStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public OrderResponse create(CreateOrderRequest request) {

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = request.getItems().stream().map(i -> {
            OrderItem item = new OrderItem();
            item.setProductName(i.getProductName());
            item.setQuantity(i.getQuantity());
            item.setPrice(i.getPrice());
            return item;
        }).toList();

        order.setItems(items);

        var total = items.stream()
                .map(i -> i.getPrice().multiply(java.math.BigDecimal.valueOf(i.getQuantity())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        order.setTotalAmount(total);

        Order saved = repository.save(order);
        return new OrderResponse(saved.getId(), saved.getStatus(), saved.getTotalAmount());
    }

    public Order get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public Page<Order> list(OrderStatus status, Pageable pageable) {
        return status == null
                ? repository.findAll(pageable)
                : repository.findByStatus(status, pageable);
    }

    public void cancel(Long id) {
        Order order = get(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Only PENDING orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        repository.save(order);
    }

    public void updateStatus(Long id, OrderStatus next) {
        Order order = get(id);

        if (!OrderStatusValidator.isValid(order.getStatus(), next)) {
            throw new InvalidOrderStateException("Invalid status transition");
        }

        order.setStatus(next);
        repository.save(order);
    }
}
