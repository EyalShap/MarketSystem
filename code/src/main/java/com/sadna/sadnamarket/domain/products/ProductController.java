package com.sadna.sadnamarket.domain.products;

import com.sadna.sadnamarket.domain.users.UserFacade;

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

    // there should be all the product fields as parameters
    public int addProduct(int storeId, String productName) {
        // dana added this proxy function for stock management use case
        // assuming it returns the product id of the new product
        nextProductId++;
        return nextProductId - 1;
    }

    public void removeProduct(int productId) {
        // dana added this proxy function for stock management use case
    }

    // there should be all the product fields as parameters
    public void updateProduct(int productId, String newProductName) {
        // dana added this proxy function for stock management use case
    }
}
