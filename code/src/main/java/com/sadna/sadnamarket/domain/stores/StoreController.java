package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.products.ProductController;
import com.sadna.sadnamarket.domain.users.UserController;

import java.util.HashMap;

public class StoreController {
    private static StoreController instance;
    private int nextStoreId;
    private HashMap<Integer, Store> stores;

    private StoreController() {
        this.nextStoreId = 0;
        this.stores = new HashMap<>();
    }

    public static StoreController getInstance() {
        if (instance == null) {
            instance = new StoreController();
        }
        return instance;
    }

    // returns id of newly created store
    public int createStore(int founderId, String storeName) {
        if(!UserController.getInstance().canCreateStore(founderId))
            throw new IllegalArgumentException(String.format("User with id %d can not create a new store.", founderId));
        if(storeIdExists(nextStoreId))
            throw new IllegalArgumentException(String.format("A store with the id \"%d\" already exists.", nextStoreId));
        if(storeNameExists(storeName))
            throw new IllegalArgumentException(String.format("A store with the name \"%s\" already exists.", storeName));

        Store createdStore = new Store(nextStoreId, storeName, founderId);
        stores.put(nextStoreId, createdStore);
        nextStoreId++;
        return nextStoreId - 1;
    }

    private boolean storeIdExists(int storeId) {
        return stores.containsKey(storeId);
    }

    private boolean storeNameExists(String storeName) {
        for(Store store : stores.values()) {
            if(store.getStoreName().equals(storeName)) {
                return true;
            }
        }
        return false;
    }

    public int addProductToStore(int userId, int storeId, String productName) {
        if(!UserController.getInstance().canAddProductsToStore(userId, storeId))
            throw new IllegalArgumentException(String.format("User with id %d can not add a product to store with id %d.", userId, storeId));
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));

        int newProductId = ProductController.getInstance().addProduct(storeId, productName);
        stores.get(storeId).addProduct(newProductId);
        return newProductId;
    }
}
