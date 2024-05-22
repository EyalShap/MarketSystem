package com.sadna.sadnamarket.domain.users;

public class Request extends Notification{
    private String senderName;
    private int storeId;
    private String role;

    public Request(String senderName,String msg, int storeId,String role) {
        super(msg);
        this.senderName=senderName;
        this.storeId=storeId;
        this.role = role;
    }
    @Override
    public void acceptOwner(Member accepting) {
        accepting.addRole(new StoreOwner(storeId,senderName));
    }

    @Override
    public void acceptManager(Member accepting) {
        accepting.addRole(new StoreManager(storeId));

    }

    @Override
    public void reject(){

    }
    @Override
    public String toString(){
        return super.toString()+" from store "+storeId+" from user: "+senderName +"as" +role;
    }
}
