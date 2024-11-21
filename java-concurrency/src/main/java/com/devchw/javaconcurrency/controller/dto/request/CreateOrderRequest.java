package com.devchw.javaconcurrency.controller.dto.request;

public record CreateOrderRequest(
        Long userId,
        String productName
) {
}
