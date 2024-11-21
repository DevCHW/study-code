package com.devchw.javaconcurrency.controller;

import com.devchw.javaconcurrency.controller.dto.request.CreateOrderRequest;
import com.devchw.javaconcurrency.controller.dto.response.CreateOrderResponse;
import com.devchw.javaconcurrency.controller.dto.response.OrderResponse;
import com.devchw.javaconcurrency.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class OrderController {

    private final OrderService orderService;
    private static final Logger logger = Logger.getLogger(OrderController.class.getName());

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/orders")
    public ResponseEntity<CreateOrderResponse> order(@RequestBody CreateOrderRequest request) {
        logger.info("주문 API 호출, userId=" + request.userId() + " productName=" + request.productName());
        CreateOrderResponse data = orderService.order(request.userId(), request.productName());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/api/v1/orders")
    public ResponseEntity<List<OrderResponse>> getOrders() {
        List<OrderResponse> data = orderService.findAll();
        return ResponseEntity.ok(data);
    }

}
