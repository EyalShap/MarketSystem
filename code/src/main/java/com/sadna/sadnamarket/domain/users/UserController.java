package com.sadna.sadnamarket.domain.users;

import java.util.HashMap;
import java.util.NoSuchElementException;
;


//this is the facade of the Users package
//it merely exists so the folder appears on git
//you may delete these comments when beggining work
//have fun :)
public class UserController {
    private static UserController instance;
    private static HashMap<String,Member> members;
    private static HashMap<String,Member> userNameAndPassword;
    private static String systemManagerUserName;

    private UserController() {
        members=new HashMap<>();
        userNameAndPassword=new HashMap<>();
        systemManagerUserName=null;
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }  
    
    public boolean canAddProductsToStore(int userId, int storeId) {
        // Dana added this proxy function for the stock management use case
        return true;
    }

    public boolean canDeleteProductsFromStore(int userId, int storeId) {
        // Dana added this proxy function for the stock management use case
        return true;
    }

    public boolean canUpdateProductsInStore(int userId, int storeId) {
        // Dana added this proxy function for the stock management use case
        return true;
    }

    public void notify(int userId, String msg) {
        // Dana added this proxy function for the close store use case
    }
    public void login(String userName,String password){
        if(!hasUser(userName))
            throw new NoSuchElementException("User doesnt exist in system");
        
    }

    private boolean hasUser(String userName){
        return userNameAndPassword.containsKey(userName);
    }
    /*
    public void addStoreOwnerRole(int currentOwnerId, int newOwnerId, int storeId) {
        // Dana added this proxy function for adding a new owner use case
        // Assuming this function checks if currentOwnerId can assign new owners to storeId
    }

    public void addStoreManagerRole(int currentOwnerId, int newManagerId, int storeId, Set<Integer> managerPermissions) {
        // Dana added this proxy function for adding a new manager use case
        // Assuming this function checks if currentOwnerId can assign managers to storeId
    }

    private Set<ManagerPermission> getPermissions(Set<Integer> managerPermissions) {
        Set<ManagerPermission> res = new HashSet<>();
        for(int permission : managerPermissions) {
            res.add(ManagerPermission.getPermission(permission));
        }
        return res;
    }*/
}
