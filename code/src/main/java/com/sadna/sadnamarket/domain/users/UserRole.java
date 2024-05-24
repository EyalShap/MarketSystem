package com.sadna.sadnamarket.domain.users;

import java.util.List;

public interface UserRole {
    int getStoreId();
    boolean hasPermission(Permission permission);
    void addPermission(Permission permission);
    void removePermission(Permission permission);
    boolean isApointedByUser(String username);
    List<String> getAppointers(); 
    public void leaveRole(UserRoleVisitor userRoleVisitor,int storeId,Member member,UserFacade userFacade);
    void sendRequest(UserFacade userFacade,String senderName,String sentName,String reqType);
    String getApointee();
} 
