package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;

public class StoreOwner implements UserRole {
    private int storeId;
    private List<String> appointments;

    public StoreOwner(int storeId){
        this.storeId=storeId;
        appointments=new ArrayList<String>();
    }
    @Override
    public boolean hasPermission(Permission permission){
        if(permission==Permission.REOPEN_STORE||permission==Permission.CLOSE_STORE)
            return false;
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
    @Override
    public String toString(){
        return "store owner of store: "+storeId;
    }
    
}
