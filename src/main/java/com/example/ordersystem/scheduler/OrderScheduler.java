
package com.example.ordersystem.scheduler;

import com.example.ordersystem.entity.*;
import com.example.ordersystem.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderRepository repository;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void processPendingOrders() {

        List<Order> pendingOrders = repository.findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING)
                .toList();

        pendingOrders.forEach(o -> o.setStatus(OrderStatus.PROCESSING));

        repository.saveAll(pendingOrders);
    }
}
