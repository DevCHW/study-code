package com.study.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CompletableFutureBasicTest {

    @Test
    void supplyAsync() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

        // when
        String result = messageFuture.join();

        // then
        assertThat(result).isEqualTo(message);
    }

    @Test
    void runAsync() {
        // given
        String message = "hello";
        CompletableFuture<Void> messageFuture = CompletableFuture.runAsync(() -> PrintUtil.print(message));

        // when
        messageFuture.join();
    }

    @Test
    void completedFuture() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.completedFuture(message);

        // when
        String result = messageFuture.join();

        // then
        assertThat(result).isEqualTo("hello");
    }

    @Test
    void thenApply() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

        // when
        String result = messageFuture
                .thenApply(printMessage -> printMessage + " world")
                .join();

        // then
        assertThat(result).isEqualTo("hello world");
    }

    @Test
    void thenAccept() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

        // when
        messageFuture
                .thenAccept(printMessage -> {
                    String result = printMessage + " world";
                    log.info("result ={}", result);
                })
                .join();
    }

    @Test
    void exceptionally() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.printException(message));

        // when
        String result = messageFuture
                .exceptionally(exception -> {
                    if (exception instanceof RuntimeException) {
                        return "런타임 에러가 났나봐요..";
                    }
                    return null;
                })
                .join();

        // then
        assertThat(result).isEqualTo("런타임 에러가 났나봐요..");
    }

    @Test
    void handle() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

        // when
        String result = messageFuture
                .handle((printMessage, throwable) -> printMessage + "world")
                .join();

        // then
        assertThat(result).isEqualTo("hello world");
    }

    @Test
    void allOf() {
        // given
        List<String> messages = Arrays.asList("apple", "banana", "water");

        List<CompletableFuture<String>> messageFutures = messages.stream()
                .map(message -> CompletableFuture.supplyAsync(() -> PrintUtil.print(message)))
                .toList();

        // when
        List<String> result = CompletableFuture.allOf(messageFutures.toArray(messageFutures.toArray(new CompletableFuture[0])))
                .thenApply(Void -> messageFutures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .join();

        // then
        assertThat(result).isEqualTo(messages);
    }

    @Test
    void thenCompose() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

        // when
        String result = messageFuture
                .thenCompose(printMessage -> CompletableFuture.supplyAsync(() -> printMessage + " world"))
                .join();

        // then
        assertThat(result).isEqualTo("hello world");
    }

    @Test
    void thenCombine() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

        // when
        String result = messageFuture
                .thenCombine(CompletableFuture.supplyAsync(() -> " world"), (message1, message2) -> message1 + message2)
                .join();

        // then
        assertThat(result).isEqualTo("hello world");
    }


}
