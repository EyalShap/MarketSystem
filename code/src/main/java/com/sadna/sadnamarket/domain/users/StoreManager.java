package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;

public class StoreManager implements UserRole {
    private int storeId;
    private List<Permission> permissions;
    private List<String> appointments;

    public StoreManager(int storeId){
        this.storeId=storeId;
        permissions=new ArrayList<>();
    }
    @Override
    public void leaveRole(UserRoleVisitor userRoleVisitor,int storeId,Member member,UserFacade userFacade) {
        userRoleVisitor.visitStoreManager(this,this.getStoreId());
    }
    public void addPermission(Permission permission){
        permissions.add(permission);
    }
    public void removePermission(Permission permission){
        if( hasPermission(permission))
            removePermission(permission);
        else
           throw new IllegalArgumentException("user doesnt has this permission");
    }
    public boolean hasPermission(Permission permission){
        return permissions.contains(permission);
    }
    public int getStoreId(){
        return storeId;
    }
    
    @Override
    public String toString(){
        return "store founder of store: "+getStoreId();
    }
    @Override
    public boolean isApointedByUser(String username) {
        return false;
    }
    @Override
    public List<String> getAppointers() {
       return appointments;
    }
}
