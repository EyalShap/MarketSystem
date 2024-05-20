package com.sadna.sadnamarket.domain.users;

public interface UserRole {
    void leaveRole();
    int getStoreId();
    boolean hasPermission(Permission permission);
    
} 
