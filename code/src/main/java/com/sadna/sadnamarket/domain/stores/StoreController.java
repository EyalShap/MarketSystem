package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.products.ProductController;
import com.sadna.sadnamarket.domain.users.UserController;
import org.apache.catalina.User;

import java.util.HashMap;
import java.util.List;

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
        // if(!UserController.getInstance().canCreateStore(founderId))
        //     throw new IllegalArgumentException(String.format("User with id %d can not create a new store.", founderId));
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
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        int newProductId = ProductController.getInstance().addProduct(storeId, productName);
        stores.get(storeId).addProduct(newProductId);
        return newProductId;
    }

    public int deleteProduct(int userId, int storeId, int productId) {
        if(!UserController.getInstance().canDeleteProductsFromStore(userId, storeId))
            throw new IllegalArgumentException(String.format("User with id %d can not delete a product from store with id %d.", userId, storeId));
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        stores.get(storeId).deleteProduct(productId);
        ProductController.getInstance().removeProduct(productId);
        return productId;
    }

    public int updateProduct(int userId, int storeId, int productId, String newProductName) {
        if(!UserController.getInstance().canUpdateProductsInStore(userId, storeId))
            throw new IllegalArgumentException(String.format("User with id %d can not update a product in store with id %d.", userId, storeId));
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!stores.get(storeId).productExists(productId))
            throw new IllegalArgumentException(String.format("A store with id %d does not have a product with id %d.", storeId, productId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        ProductController.getInstance().updateProduct(productId, newProductName);
        return productId;
    }

    /*
    public int addStoreOwner(int currentOwnerId, int newOwnerId, int storeId) {
        stores.get(storeId).addStoreOwner(currentOwnerId, newOwnerId);
        return newOwnerId;
    }

    public int addStoreManager(int currentOwnerId, int newManagerId, int storeId) {
        stores.get(storeId).addStoreManager(currentOwnerId, newManagerId);
        return newManagerId;
    }*/

    public boolean closeStore(int userId, int storeId) {
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));

        stores.get(storeId).closeStore(userId);

        String msg = String.format("The store \"%s\" was closed.", stores.get(storeId).getStoreName());
        List<Integer> ownerIds = stores.get(storeId).getOwnerIds();
        List<Integer> managerIds = stores.get(storeId).getManagerIds();
        for(int ownerId : ownerIds) {
            UserController.getInstance().notify(ownerId, msg);
        }
        for(int managerId : managerIds) {
            UserController.getInstance().notify(managerId, msg);
        }

        return true;
    }

    private boolean isStoreActive(int storeId) {
        return stores.get(storeId).getIsActive();
    }

}
