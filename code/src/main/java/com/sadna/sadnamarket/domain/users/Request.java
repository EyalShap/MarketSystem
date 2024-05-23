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
    private void acceptOwner(Member accepting) {
        accepting.addRole(new StoreOwner(storeId,senderName));
    }

    private void acceptManager(Member accepting) {
        accepting.addRole(new StoreManager(storeId));

    }
    public void accept(Member accepting) {
        if(role.equals("Manager"))
            acceptManager(accepting);
        else
            acceptOwner(accepting);

    }


    @Override
    public void reject(){
        
    }
    @Override
    public String toString(){
        return super.toString()+" from store "+storeId+" from user: "+senderName +"as" +role;
    }
}
