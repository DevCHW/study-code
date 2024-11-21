package com.devchw.javaconcurrency.service.mutex;

import com.devchw.javaconcurrency.repository.OrderRepository;
import com.devchw.javaconcurrency.service.AlreadyOrderedProductException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class MutexOrderServiceTest {

    private OrderRepository orderRepository = new OrderRepository();
    private MutexOrderService orderService = new MutexOrderService(orderRepository);

    @Test
    void 같은_이름의_상품을_동시에_주문하더라도_하나만_성공해야한다() throws InterruptedException {
        // given
        Long userId = 1L;
        String productName = "애플워치";

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    orderService.order(userId, productName);
                    successCount.getAndIncrement();
                } catch (AlreadyOrderedProductException e) {
                    failCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(threadCount - 1);
    }
}