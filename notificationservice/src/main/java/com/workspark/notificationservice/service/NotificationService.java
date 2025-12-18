package com.workspark.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.commonconfig.service.impl.RedisSubServiceImpl;
import com.workspark.models.enums.NotificationMode;
import com.workspark.models.pojo.Notification;
import com.workspark.notificationservice.exceptions.customExceptions.NotificationException;
import com.workspark.notificationservice.factory.NotificationChannelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class is responsible for handling notifications received from Redis.
 * It implements the RedisSubService interface and is annotated with @Service.
 * 
 * Concepts demonstrated:
 * - Parallel notification channel execution
 * - CompletableFuture for concurrent operations
 * - Async processing with @Async
 * - Thread-safe result tracking
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService extends RedisSubServiceImpl {

    private final NotificationChannelFactory notificationServiceFactory;
    
    /**
     * Thread-safe map to track notification results.
     * Key: Notification ID or mode, Value: Success count
     * 
     * Concepts:
     * - ConcurrentHashMap: Thread-safe map implementation
     * - AtomicInteger: Thread-safe integer operations
     */
    private final ConcurrentHashMap<String, AtomicInteger> notificationStats = new ConcurrentHashMap<>();

    /**
     * This method is called when a message is received from the Redis channel.
     * It parses the message, determines the notification modes, and sends the notification 
     * to the appropriate channels IN PARALLEL.
     * 
     * Concepts demonstrated:
     * - Parallel channel execution using CompletableFuture
     * - CompletableFuture.allOf() for waiting on all channels
     * - Exception handling per channel (one failure doesn't stop others)
     * - Thread-safe result tracking
     *
     * @param message The message received from the Redis channel
     */
    @Override
    @Async("notificationExecutor")
    public void handleMessage(Message message) {
        log.info("{}", message);
        String jsonMessage = new String(message.getBody());
        log.info("Received message: {}", jsonMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        Notification notificationDto = null;
        try {
            notificationDto = objectMapper.readValue(jsonMessage, Notification.class);
        } catch (JsonProcessingException e) {
            throw new NotificationException("Error parsing JSON message: " + e.getMessage());
        }
        Set<NotificationMode> modes = this.getNotificationModes(notificationDto);

        // Send notifications to all channels IN PARALLEL
        sendNotificationsParallel(notificationDto, modes);
    }
    
    /**
     * Sends notifications to multiple channels in parallel.
     * 
     * Concepts demonstrated:
     * - CompletableFuture.supplyAsync(): Creates async task for each channel
     * - CompletableFuture.allOf(): Waits for all channels to complete
     * - Parallel execution: All channels execute concurrently
     * - Exception handling: Each channel's failures are handled independently
     * - Result aggregation: Collects results from all channels
     * 
     * Performance benefits:
     * - If sending email, SMS, and push sequentially takes 3 seconds (1 sec each)
     * - Parallel sending takes ~1 second (all run concurrently)
     * 
     * @param notification Notification to send
     * @param modes Set of notification modes (channels) to use
     */
    private void sendNotificationsParallel(Notification notification, Set<NotificationMode> modes) {
        log.info("Sending notification to {} channels in parallel: {}", modes.size(), modes);
        
        // Create a CompletableFuture for each notification channel
        // Each channel will execute asynchronously and in parallel
        Set<CompletableFuture<Boolean>> futures = modes.stream()
                .map(mode -> CompletableFuture.supplyAsync(() -> {
                    try {
                        NotificationChannel channel = notificationServiceFactory.getChannel(mode);
                        if (channel != null) {
                            log.debug("Sending {} notification...", mode);
                            boolean success = channel.send(notification);
                            
                            if (success) {
                                log.info("{} notification sent successfully", mode);
                                // Update statistics thread-safely
                                notificationStats.computeIfAbsent(mode.name(), k -> new AtomicInteger(0))
                                        .incrementAndGet();
                                return true;
                            } else {
                                log.error("Failed to send {} notification", mode);
                                return false;
                            }
                        } else {
                            log.error("No channel found for mode: {}", mode);
                            return false;
                        }
                    } catch (Exception e) {
                        log.error("Error sending {} notification", mode, e);
                        // Exception doesn't stop other channels from executing
                        return false;
                    }
                }))
                .collect(Collectors.toSet());
        
        // Wait for all notification channels to complete
        // CompletableFuture.allOf() returns a CompletableFuture<Void> that completes
        // when all the provided futures complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        
        // Process results once all channels complete
        allFutures.thenRun(() -> {
            long successCount = futures.stream()
                    .map(CompletableFuture::join) // Get result (blocks until complete, but we know it's done)
                    .filter(Boolean::booleanValue)
                    .count();
            
            log.info("Notification processing completed. {} out of {} channels succeeded", 
                    successCount, modes.size());
        }).exceptionally(throwable -> {
            log.error("Error in parallel notification processing", throwable);
            return null;
        });
    }
    
    /**
     * Legacy method: Sequential notification sending (kept for comparison).
     * This method sends notifications sequentially - one after another.
     * 
     * @param notification Notification to send
     * @param modes Set of notification modes
     */
    @SuppressWarnings("unused")
    private void sendNotificationsSequential(Notification notification, Set<NotificationMode> modes) {
        log.info("Sending notification to {} channels sequentially: {}", modes.size(), modes);
        
        Notification finalNotificationDto = notification;
        modes.forEach(mode -> {
            NotificationChannel service = notificationServiceFactory.getChannel(mode);
            if (service != null) {
                boolean success = service.send(finalNotificationDto);
                if (success) {
                    log.info("{} notification sent successfully", mode);
                } else {
                    log.error("Failed to send {} notification", mode);
                }
            } else {
                log.error("No channel found for mode: {}", mode);
            }
        });
    }

    /**
     * This method determines the notification modes based on the notification DTO.
     *
     * @param notificationDto The notification DTO
     * @return Set of NotificationMode
     */
    private Set<NotificationMode> getNotificationModes(Notification notificationDto) {
        Set<NotificationMode> modes = new HashSet<>();

        if(notificationDto.getEmail() != null) {
            modes.add(NotificationMode.EMAIL);
        }

        return modes;
    }
}
