package com.devchw.javaconcurrency.service.semaphore;

import com.devchw.javaconcurrency.controller.dto.response.CreateOrderResponse;
import com.devchw.javaconcurrency.controller.dto.response.OrderResponse;
import com.devchw.javaconcurrency.repository.OrderEntity;
import com.devchw.javaconcurrency.repository.OrderRepository;
import com.devchw.javaconcurrency.service.AlreadyOrderedProductException;
import com.devchw.javaconcurrency.service.OrderService;

import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaphoreOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final Semaphore semaphore = new Semaphore(1, true);

    public SemaphoreOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public CreateOrderResponse order(Long userId, String productName) {
        try {
            semaphore.acquire();
            boolean exists = orderRepository.existsByProductName(productName);

            if (exists) {
                throw new AlreadyOrderedProductException("Product already ordered.");
            }

            OrderEntity savedOrder = orderRepository.save(new OrderEntity(userId, productName));
            return CreateOrderResponse.from(savedOrder);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
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
