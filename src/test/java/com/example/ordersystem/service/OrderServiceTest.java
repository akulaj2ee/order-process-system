package com.example.ordersystem.service;

import com.example.ordersystem.dto.*;
import com.example.ordersystem.entity.*;
import com.example.ordersystem.exception.InvalidOrderStateException;
import com.example.ordersystem.exception.OrderNotFoundException;
import com.example.ordersystem.repository.OrderRepository;
import com.example.ordersystem.validator.OrderStatusValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ========================
    // CREATE ORDER
    // ========================
    @Test
    void shouldCreateOrderSuccessfully() {
        CreateOrderRequest request = new CreateOrderRequest();

        OrderItemRequest item = new OrderItemRequest();
        item.setProductName("Laptop");
        item.setPrice(BigDecimal.valueOf(1000));
        item.setQuantity(2);

        request.setItems(List.of(item));

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setTotalAmount(BigDecimal.valueOf(2000));

        when(repository.save(any())).thenReturn(savedOrder);

        OrderResponse response = service.create(request);

        assertEquals(1L, response.getId());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertEquals(BigDecimal.valueOf(2000), response.getTotalAmount());

        verify(repository).save(any());
    }

    // ========================
    // GET ORDER
    // ========================
    @Test
    void shouldReturnOrderWhenExists() {
        Order order = new Order();
        order.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        Order result = service.get(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.get(1L));
    }

    // ========================
    // LIST ORDERS
    // ========================
    @Test
    void shouldReturnAllOrdersWhenStatusIsNull() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Order> page = new PageImpl<>(List.of(new Order()));
        when(repository.findAll(pageable)).thenReturn(page);

        Page<Order> result = service.list(null, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldReturnFilteredOrders() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Order> page = new PageImpl<>(List.of(new Order()));
        when(repository.findByStatus(OrderStatus.PENDING, pageable)).thenReturn(page);

        Page<Order> result = service.list(OrderStatus.PENDING, pageable);

        assertEquals(1, result.getTotalElements());
    }

    // ========================
    // CANCEL ORDER
    // ========================
    @Test
    void shouldCancelPendingOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        service.cancel(1L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(repository).save(order);
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonPendingOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.SHIPPED);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStateException.class, () -> service.cancel(1L));
    }

    // ========================
    // UPDATE STATUS
    // ========================
    @Test
    void shouldUpdateStatusSuccessfully() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        service.updateStatus(1L, OrderStatus.PROCESSING);

        assertEquals(OrderStatus.PROCESSING, order.getStatus());
        verify(repository).save(order);
    }

    @Test
    void shouldThrowExceptionForInvalidStatusTransition() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.DELIVERED);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStateException.class,
                () -> service.updateStatus(1L, OrderStatus.PENDING));
    }
}