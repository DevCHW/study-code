package com.devchw.javaconcurrency.service.mutex;

import com.devchw.javaconcurrency.controller.dto.response.CreateOrderResponse;
import com.devchw.javaconcurrency.controller.dto.response.OrderResponse;
import com.devchw.javaconcurrency.repository.OrderEntity;
import com.devchw.javaconcurrency.repository.OrderRepository;
import com.devchw.javaconcurrency.service.AlreadyOrderedProductException;
import com.devchw.javaconcurrency.service.OrderService;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutexOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final Lock lock = new ReentrantLock();

    public MutexOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public CreateOrderResponse order(Long userId, String productName) {
        try {
            lock.lock();
            boolean exists = orderRepository.existsByProductName(productName);

            if (exists) {
                throw new AlreadyOrderedProductException("Product already ordered.");
            }

            OrderEntity savedOrder = orderRepository.save(new OrderEntity(userId, productName));
            return CreateOrderResponse.from(savedOrder);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<OrderResponse> findAll() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return orderEntities.stream()
                .map(OrderResponse::from)
                .toList();
    }

}
