package com.garage_guru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java 21: Virtual Threads Configuration (Project Loom)
 *
 * Virtual threads are lightweight threads that dramatically reduce the effort of writing,
 * maintaining, and observing high-throughput concurrent applications.
 *
 * Benefits:
 * - Millions of virtual threads can be created without overwhelming system resources
 * - Simplified concurrent programming model (write synchronous code that scales)
 * - Perfect for I/O-bound tasks (database calls, HTTP requests, etc.)
 * - No need for reactive programming patterns for scalability
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Java 21: Virtual thread executor using Project Loom.
     * Creates a new virtual thread for each task.
     *
     * Virtual threads are ideal for:
     * - Database operations
     * - External API calls
     * - File I/O operations
     * - Any blocking operation
     */
    @Bean(name = "virtualThreadExecutor")
    public ExecutorService virtualThreadExecutor() {
        // Java 21: Creates an ExecutorService that starts a new virtual thread for each task
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * Java 21: Task executor that uses virtual threads for Spring's @Async methods.
     * This enables async methods to run on virtual threads automatically.
     */
    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        // For Spring's @Async support, we still use ThreadPoolTaskExecutor
        // but with virtual threads enabled in the JVM
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.setVirtualThreads(true); // Java 21: Enable virtual threads
        executor.initialize();
        return executor;
    }
}
