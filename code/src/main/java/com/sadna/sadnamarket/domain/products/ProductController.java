package com.sadna.sadnamarket.domain.products;

import com.sadna.sadnamarket.domain.users.UserController;

//this is the facade of the Products package
//it merely exists so the folder appears on git
//you may delete these comments when beggining work
//have fun :)
public class ProductController {
    private static ProductController instance;
    private int nextProductId = 0;

    private ProductController() {
    }

    public static ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }



}
