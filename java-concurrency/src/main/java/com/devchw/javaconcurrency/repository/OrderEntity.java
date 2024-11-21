package com.devchw.javaconcurrency.repository;

import java.time.LocalDateTime;

public class OrderEntity {
    private Long id;
    private Long userId;
    private String productName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderEntity(Long userId, String productName) {
        this.userId = userId;
        this.productName = productName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getProductName() {
        return productName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
