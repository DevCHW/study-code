package com.study.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class CompletableFutureAsyncTest {

    @Autowired
    private Executor threadPoolTaskExecutor;

    @Test
    @DisplayName("thenApply 메소드에 스레드 풀을 지정하지 않으면 supplyAsync과 같은 스레드에서 진행한다.")
    void simpleThenApply() {
        // given
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() ->
                PrintUtil.print(Thread.currentThread().getName()), threadPoolTaskExecutor);

        // when
        String result = messageFuture
                .thenApply(printMessage -> printMessage + " " + Thread.currentThread().getName())
                .join();

        // then
        String[] threadPoolNameArr = result.split(" ");
        assertThat(threadPoolNameArr[0]).isEqualTo(threadPoolNameArr[1]);
    }

    @Test
    @DisplayName("thenApplyAsync 메소드에 스레드 풀을 지정하지 않으면 기본 스레드 풀의 새로운 스레드가 async하게 진행한다.")
    void thenApplyAsyncWithNotThreadPool() {
        // given
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() ->
                PrintUtil.print(Thread.currentThread().getName()), threadPoolTaskExecutor);

        // when
        String result = messageFuture
                .thenApplyAsync(printMessage -> printMessage + " " + Thread.currentThread().getName())
                .join();

        // then
        String[] threadPoolNameArr = result.split(" ");
        assertThat(threadPoolNameArr[0]).isNotEqualTo(threadPoolNameArr[1]);
    }

    @Test
    @DisplayName("thenApplyAsync 메소드에 스레드 풀을 지정하면 지정한 스레드 풀의 새로운 스레드가 async하게 진행한다.")
    void thenApplyAsyncWithThreadPool() {
        // given
        String threadNamePrefix = AsyncConfig.THREAD_NAME_PREFIX;
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() ->
                PrintUtil.print(Thread.currentThread().getName()), threadPoolTaskExecutor);

        // when
        String result = messageFuture
                .thenApplyAsync(printMessage ->
                        printMessage + " " + Thread.currentThread().getName(),
                        threadPoolTaskExecutor)
                .join();

        // then
        String[] threadPoolNameArr = result.split(" ");
        assertThat(threadPoolNameArr[0]).startsWith(threadNamePrefix);
        assertThat(threadPoolNameArr[1]).startsWith(threadNamePrefix);
    }

}
