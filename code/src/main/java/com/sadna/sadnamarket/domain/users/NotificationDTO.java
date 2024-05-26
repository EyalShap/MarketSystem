package com.sadna.sadnamarket.domain.users;

import java.time.LocalDateTime;

public class NotificationDTO {
    private String message;
    private int id;
    private LocalDateTime date;

    public NotificationDTO(String message,LocalDateTime date,int id) {
        this.message=message;
        this.date=date;
        this.id=id;
    }

    public NotificationDTO(Notification notific) {
        this.message = notific.getMessage();
        this.date = notific.getDate();
        this.id=notific.getId();
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
