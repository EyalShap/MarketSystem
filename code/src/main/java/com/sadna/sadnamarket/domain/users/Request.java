package com.sadna.sadnamarket.domain.users;

public class Request extends Notification{
    private String senderName;
    private int storeId;
    public Request(String senderName,String msg, int storeId) {
        
        super(msg);
        this.senderName=senderName;
        this.storeId=storeId;
    }
    @Override
    public void accept() {
        
    }
    @Override
    public void reject(){

    }
    @Override
    public String toString(){
        return super.toString()+" from store "+storeId+" from user: "+senderName;
    }
}
