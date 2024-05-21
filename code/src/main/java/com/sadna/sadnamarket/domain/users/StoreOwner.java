package com.sadna.sadnamarket.domain.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

public class StoreOwner implements UserRole {
    private int storeId;
    private List<String> appointments;
    private String apointee;

    public StoreOwner(int storeId,String apointee){
        this.storeId=storeId;
        appointments=new ArrayList<String>();
        this.apointee=apointee;
    }
    @Override
    public boolean hasPermission(Permission permission){
        if(permission==Permission.REOPEN_STORE||permission==Permission.CLOSE_STORE)
            return false;
        return true;
    }
   
    public int getStoreId(){
        return storeId;
    }
    @Override
    public String toString(){
        return "store owner of store: "+storeId;
    }
    @Override
    public void addPermission(Permission permission) {
        throw new IllegalStateException("store owner has all the roles");
    }
    @Override
    public boolean isApointedByUser(String username) {
        return appointments.contains(username);
    }
    @Override
    public List<String> getAppointers() {
       return appointments;
    }
    @Override
    public void leaveRole(UserRoleVisitor userRoleVisitor,int storeId,Member member,UserFacade userFacade) {
        userRoleVisitor.visitStoreOwner(this, storeId,member,userFacade);
    }

    public void addOwner(UserFacade userFacade,String senderName,String sentName){
        if(sentName.equals(apointee)){
            throw new IllegalAccessError("you cant apoint the one who apointed you");
        }
        userFacade.addRequest(senderName,sentName,"Hi, You have a request to be a Owener at: "+storeId,storeId);
    }
    @Override
    public void addManager(UserFacade userFacade, String senderName, String sentName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addManager'");
    }
}
