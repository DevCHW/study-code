package com.devchw.javaconcurrency.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    private static final Map<Long, OrderEntity> orderMap = new HashMap<>();
    private static Long orderId = 0L;

    public OrderEntity findById(Long id) {
        if (!orderMap.containsKey(id)) throw new IllegalStateException("Order not found.");
        return orderMap.get(id);
    }

    public OrderEntity save(OrderEntity order) {
        Long newOrderId = ++orderId;
        order.setId(newOrderId);
        orderMap.put(newOrderId, order);
        return orderMap.get(newOrderId);
    }

    public boolean existsByProductName(String productName) {
        return orderMap.values().stream()
                .anyMatch(order -> order.getProductName() != null && order.getProductName().equals(productName));
    }

    public List<OrderEntity> findAll() {
        return orderMap.values().stream().toList();
    }
}
