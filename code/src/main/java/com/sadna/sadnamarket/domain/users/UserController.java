package com.sadna.sadnamarket.domain.users;

import com.sadna.sadnamarket.domain.stores.StoreFacade;

import java.util.HashSet;
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



}
