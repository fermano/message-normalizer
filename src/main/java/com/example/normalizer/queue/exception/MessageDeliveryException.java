package com.example.normalizer.queue.exception;

public class MessageDeliveryException extends RuntimeException {
    public MessageDeliveryException(String message) {
        super(message);
    }
    public MessageDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
