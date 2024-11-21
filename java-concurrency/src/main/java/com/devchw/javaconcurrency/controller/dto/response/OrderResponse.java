package com.devchw.javaconcurrency.controller.dto.response;

import com.devchw.javaconcurrency.repository.OrderEntity;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long userId,
        String productName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OrderResponse from(OrderEntity orderEntity) {
        return new OrderResponse(
                orderEntity.getId(),
                orderEntity.getUserId(),
                orderEntity.getProductName(),
                orderEntity.getCreatedAt(),
                orderEntity.getUpdatedAt()
        );
    }
}
