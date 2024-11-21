package com.devchw.javaconcurrency.config;

import com.devchw.javaconcurrency.repository.OrderRepository;
import com.devchw.javaconcurrency.service.OrderService;
import com.devchw.javaconcurrency.service.concurrencyissue.ConcurrencyIssueOrderService;
import com.devchw.javaconcurrency.service.mutex.MutexOrderService;
import com.devchw.javaconcurrency.service.synchronize.SynchronizedOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceConfig {

    @Bean
    public OrderService orderService(OrderRepository orderRepository) {
        return new ConcurrencyIssueOrderService(orderRepository); // 동시성 이슈가 있는 주문
//        return new SemaphoreOrderService(orderRepository); // 세마포어 적용 주문 서비스
//        return new MutexOrderService(orderRepository); // 뮤텍스 적용 주문 서비스
//        return new SynchronizedOrderService(orderRepository); // synchronized 적용 주문 서비스
    }
}
