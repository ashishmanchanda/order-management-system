package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.CreateOrderDTO;
import com.example.ordermanagement.dto.CreateOrderItemDTO;
import com.example.ordermanagement.entity.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for OrderController
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateOrder() throws Exception {
        CreateOrderDTO createOrderDTO = CreateOrderDTO.builder()
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .shippingAddress("123 Main St")
                .orderItems(List.of(
                        CreateOrderItemDTO.builder()
                                .productName("Product 1")
                                .productCode("PROD-001")
                                .quantity(2)
                                .unitPrice(BigDecimal.valueOf(50.00))
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalAmount").value(100.00));
    }

    @Test
    public void testCreateOrder_InvalidEmail() throws Exception {
        CreateOrderDTO createOrderDTO = CreateOrderDTO.builder()
                .customerName("John Doe")
                .customerEmail("invalid-email")
                .shippingAddress("123 Main St")
                .orderItems(List.of(
                        CreateOrderItemDTO.builder()
                                .productName("Product 1")
                                .productCode("PROD-001")
                                .quantity(2)
                                .unitPrice(BigDecimal.valueOf(50.00))
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders?pageNumber=0&pageSize=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void testGetOrdersByStatus() throws Exception {
        mockMvc.perform(get("/api/orders/status/PENDING?pageNumber=0&pageSize=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}

