package com.sadna.sadnamarket.domain.users;

import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.domain.stores.StoreController;

import java.util.HashMap;

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
}
