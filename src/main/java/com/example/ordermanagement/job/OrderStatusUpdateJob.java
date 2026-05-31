package com.example.ordermanagement.job;

import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Scheduled job to automatically update PENDING orders to PROCESSING
 */
@Component
@Slf4j
public class OrderStatusUpdateJob {

    private final OrderRepository orderRepository;

    public OrderStatusUpdateJob(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Automatically update PENDING orders to PROCESSING every 5 minutes
     * Fixed rate of 300000 milliseconds = 5 minutes
     */
    @Scheduled(fixedRate = 300000, initialDelay = 60000)
    @Transactional
    public void updatePendingOrdersToProcessing() {
        log.info("Starting scheduled job to update PENDING orders to PROCESSING");

        try {
            List<Order> pendingOrders = orderRepository.findPendingOrders();

            if (pendingOrders.isEmpty()) {
                log.info("No pending orders found to update");
                return;
            }

            log.info("Found {} pending orders to update", pendingOrders.size());

            for (Order order : pendingOrders) {
                log.debug("Updating order ID: {} from PENDING to PROCESSING", order.getId());
                order.setStatus(OrderStatus.PROCESSING);
                orderRepository.save(order);
                log.debug("Order ID: {} successfully updated to PROCESSING", order.getId());
            }

            log.info("Successfully updated {} orders from PENDING to PROCESSING", pendingOrders.size());
        } catch (Exception e) {
            log.error("Error occurred while updating pending orders", e);
        }
    }
}

