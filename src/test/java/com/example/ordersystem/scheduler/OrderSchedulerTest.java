package com.example.ordersystem.scheduler;

import com.example.ordersystem.entity.*;
import com.example.ordersystem.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderSchedulerTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderScheduler scheduler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessOnlyPendingOrders() {
        // GIVEN
        Order pendingOrder = new Order();
        pendingOrder.setId(1L);
        pendingOrder.setStatus(OrderStatus.PENDING);

        Order shippedOrder = new Order();
        shippedOrder.setId(2L);
        shippedOrder.setStatus(OrderStatus.SHIPPED);

        when(repository.findAll()).thenReturn(List.of(pendingOrder, shippedOrder));

        // WHEN
        scheduler.processPendingOrders();

        // THEN
        assertEquals(OrderStatus.PROCESSING, pendingOrder.getStatus());
        assertEquals(OrderStatus.SHIPPED, shippedOrder.getStatus());

        verify(repository).saveAll(List.of(pendingOrder));
    }

    @Test
    void shouldHandleNoPendingOrders() {
        // GIVEN
        Order deliveredOrder = new Order();
        deliveredOrder.setStatus(OrderStatus.DELIVERED);

        when(repository.findAll()).thenReturn(List.of(deliveredOrder));

        // WHEN
        scheduler.processPendingOrders();

        // THEN
        verify(repository).saveAll(List.of()); // empty list
    }
}