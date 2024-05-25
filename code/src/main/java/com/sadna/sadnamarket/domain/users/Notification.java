package com.sadna.sadnamarket.domain.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class Notification {
    private String message;
    private LocalDateTime date;
    private static final Logger logger = LogManager.getLogger(Notification.class);

    public Notification(String msg) {
        logger.info("Entering Notification constructor with msg={}", msg);
        this.message = msg;
        this.date = LocalDateTime.now();
        logger.info("Exiting Notification constructor");
    }

    public void accept(Member member) {
        logger.info("Entering accept with member={}", member);
        // No specific implementation for accept in Notification
        logger.info("Exiting accept");
    }

    @Override
    public String toString() {
        logger.info("Entering toString");
        String result = "got message: " + message + " on: " + date.toString();
        logger.info("Exiting toString with result={}", result);
        return result;
    }

    public String getMessage() {
        logger.info("Entering getMessage");
        logger.info("Exiting getMessage with result={}", message);
        return message;
    }

    public LocalDateTime getDate() {
        logger.info("Entering getDate");
        logger.info("Exiting getDate with result={}", date);
        return date;
    }
}
