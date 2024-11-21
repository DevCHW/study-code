package com.devchw.javaconcurrency.service;

import com.devchw.javaconcurrency.controller.dto.response.CreateOrderResponse;
import com.devchw.javaconcurrency.controller.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    CreateOrderResponse order(Long userId, String productName);

    List<OrderResponse> findAll();
}
