package com.sadna.sadnamarket.domain.users;

import java.time.LocalDateTime;

public class NotificationDTO {
    private String message;
    private LocalDateTime date;

    public NotificationDTO() {
        // Default constructor for serialization/deserialization
    }

    public NotificationDTO(Notification notific) {
        this.message = notific.getMessage();
        this.date = notific.getDate();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
