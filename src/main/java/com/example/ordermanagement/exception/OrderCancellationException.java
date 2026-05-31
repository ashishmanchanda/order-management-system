package com.example.ordermanagement.exception;

/**
 * Exception thrown when an order cannot be cancelled
 */
public class OrderCancellationException extends RuntimeException {
    public OrderCancellationException(String message) {
        super(message);
    }

    public OrderCancellationException(String message, Throwable cause) {
        super(message, cause);
    }
}

