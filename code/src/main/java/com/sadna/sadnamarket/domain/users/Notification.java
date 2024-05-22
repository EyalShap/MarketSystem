package com.sadna.sadnamarket.domain.users;

import java.time.LocalDateTime;

public class Notification {
    private String message;
    private LocalDateTime date;
    public Notification(String msg){
        this.message=msg;
        this.date=LocalDateTime.now();
    }
    public void acceptOwner(Member accepting){
        
    }

    public void acceptManager(Member accepting){
        
    }

    public void reject(){

    }
    @Override
    public String toString(){
        return "got message: "+ message+ " on: "+date.toString();
    }
    public String getMessage(){
        return message;
    }
    public LocalDateTime getDate(){
        return date;
    }
}
