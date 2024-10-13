package com.study.completablefuture;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    public static final String EXECUTOR_NAME = "threadPoolTaskExecutor";
    public static final String THREAD_NAME_PREFIX = "my-thread-";
    private static final int POOL_SIZE = 3;

    @Bean(name = EXECUTOR_NAME)
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(POOL_SIZE);
        executor.setMaxPoolSize(POOL_SIZE);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        return executor;
    }
}
