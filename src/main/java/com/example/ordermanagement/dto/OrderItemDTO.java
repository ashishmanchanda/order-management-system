package com.example.ordermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Order Item response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private Long id;
    private String productName;
    private String productCode;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}

