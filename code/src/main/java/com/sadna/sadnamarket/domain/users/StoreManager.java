package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;

public class StoreManager implements UserRole {
    private int storeId;
    private List<Permission> permissions;

    public StoreManager(int storeId){
        this.storeId=storeId;
        permissions=new ArrayList<>();
    }
    @Override
    public void leaveRole() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveRole'");
    }
    public void addPermission(Permission permission){

    }
    public void removePermission(Permission permission){

    }
    public boolean hasPermission(Permission permission){
        return permissions.contains(permission);
    }
    public int getStoreId(){
        return storeId;
    }
}
