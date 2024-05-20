package com.sadna.sadnamarket.domain.users;

public class StoreOwner implements UserRole {
    private int storeId;

    public StoreOwner(int storeId){
        this.storeId=storeId;
    }

    public boolean hasPermission(){
        return true;
    }
    @Override
    public void leaveRole() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveRole'");
    }
    public int getStoreId(){
        return storeId;
    }
    
}
