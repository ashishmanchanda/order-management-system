package com.example.ordermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new Order Item
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderItemDTO {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product code is required")
    private String productCode;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
}

