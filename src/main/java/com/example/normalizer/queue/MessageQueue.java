package com.example.normalizer.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageQueue {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    public void send(Object message) {
        logger.info("Queued message: {}", message);
    }
}
