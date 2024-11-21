package com.devchw.javaconcurrency.controller.dto.response;

import com.devchw.javaconcurrency.repository.OrderEntity;

import java.time.LocalDateTime;

public record CreateOrderResponse(
        Long id,
        Long userId,
        String productName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static CreateOrderResponse from(OrderEntity orderEntity) {
        return new CreateOrderResponse(
                orderEntity.getId(),
                orderEntity.getUserId(),
                orderEntity.getProductName(),
                orderEntity.getCreatedAt(),
                orderEntity.getUpdatedAt()
        );
    }
}
