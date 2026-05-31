package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for OrderRepository
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder;

    @BeforeEach
    public void setUp() {
        testOrder = Order.builder()
                .orderNumber("ORD-TEST-001")
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .shippingAddress("123 Main St")
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();
    }

    @Test
    public void testSaveOrder() {
        Order savedOrder = orderRepository.save(testOrder);

        assertNotNull(savedOrder.getId());
        assertEquals("ORD-TEST-001", savedOrder.getOrderNumber());
        assertEquals("John Doe", savedOrder.getCustomerName());
    }

    @Test
    public void testFindByOrderNumber() {
        orderRepository.save(testOrder);

        Optional<Order> foundOrder = orderRepository.findByOrderNumber("ORD-TEST-001");

        assertTrue(foundOrder.isPresent());
        assertEquals("John Doe", foundOrder.get().getCustomerName());
    }

    @Test
    public void testFindByStatus() {
        orderRepository.save(testOrder);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> foundOrders = orderRepository.findByStatus(OrderStatus.PENDING, pageable);

        assertFalse(foundOrders.isEmpty());
        assertEquals(1, foundOrders.getTotalElements());
    }

    @Test
    public void testFindByCustomerEmail() {
        orderRepository.save(testOrder);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> foundOrders = orderRepository.findByCustomerEmail("john@example.com", pageable);

        assertFalse(foundOrders.isEmpty());
        assertEquals(1, foundOrders.getTotalElements());
    }

    @Test
    public void testCountByStatus() {
        orderRepository.save(testOrder);

        long count = orderRepository.countByStatus(OrderStatus.PENDING);

        assertEquals(1, count);
    }

    @Test
    public void testFindPendingOrders() {
        orderRepository.save(testOrder);

        var pendingOrders = orderRepository.findPendingOrders();

        assertFalse(pendingOrders.isEmpty());
        assertEquals(1, pendingOrders.size());
        assertEquals(OrderStatus.PENDING, pendingOrders.get(0).getStatus());
    }
}

