package com.sadna.sadnamarket.domain.users;

import java.time.LocalDateTime;

public class Notification {
    private String message;
    private LocalDateTime date;
    public Notification(String msg){
        this.message=msg;
        this.date=LocalDateTime.now();
    }
    public void accept(){
        
    }
    public void reject(){

    }
    @Override
    public String toString(){
        return "got message: "+ message+ " on: "+date.toString();
    }
}
