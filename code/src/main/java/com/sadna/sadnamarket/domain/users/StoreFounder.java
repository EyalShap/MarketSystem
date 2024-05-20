package com.sadna.sadnamarket.domain.users;

public class StoreFounder extends StoreOwner{

    public StoreFounder(int storeId) {
        super(storeId);
    }
    @Override
    public boolean hasPermission(Permission permission){
        return true;
    }
    @Override
    public void leaveRole() {
        throw new IllegalStateException("Owner cant leave store");
    }
    @Override
    public String toString(){
        return "store founder of store: "+getStoreId();
    }
    
}
