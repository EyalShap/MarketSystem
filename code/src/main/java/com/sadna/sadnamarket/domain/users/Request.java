package com.sadna.sadnamarket.domain.users;

public class Request extends Notification{
    private int storeId;
    private int sender;
    public Request(String msg, int storeId) {
        super(msg);
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
        return super.toString()+" from store "+storeId+" from user: "+sender;
    }
}
