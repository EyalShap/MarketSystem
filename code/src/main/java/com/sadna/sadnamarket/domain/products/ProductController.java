package com.sadna.sadnamarket.domain.products;

import com.sadna.sadnamarket.domain.stores.Store;

import java.util.HashMap;
import java.util.List;

//this is the facade of the Products package
//it merely exists so the folder appears on git
//you may delete these comments when beggining work
//have fun :)
public class ProductController {
    private static ProductController instance;
    //need to check
    private HashMap<Integer, List<Product>> products;

    private ProductController(){
        products = new HashMap<>();
    }

    public static ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }

    public void addProductToStore(int storeId , String productName,int productQuantity,int price ){
        Product createdProduct=new Product(productName,productQuantity,price);
       // products.put(storeId,);
    }


}
