package com.sadna.sadnamarket.domain.users;

import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.domain.stores.StoreController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//this is the facade of the Users package
//it merely exists so the folder appears on git
//you may delete these comments when beggining work
//have fun :)
public class UserController {
    private static UserController instance;

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public boolean canCreateStore(int userId) {
        // Dana added this proxy function for the store creation use case
        return true;
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
