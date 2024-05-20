package com.sadna.sadnamarket.domain.stores;

import com.sadna.sadnamarket.domain.products.ProductController;
import com.sadna.sadnamarket.domain.users.ManagerPermission;
import com.sadna.sadnamarket.domain.users.UserController;
import com.sadna.sadnamarket.domain.users.UserDTO;
import org.apache.catalina.User;

import java.util.*;

public class StoreController {
    private static StoreController instance;
    private int nextStoreId;
    private Map<Integer, Store> stores;

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

    public int addProductToStore(int userId, int storeId, String productName, int productQuantity, int productPrice) {
        if(!UserController.getInstance().canAddProductsToStore(userId, storeId))
            throw new IllegalArgumentException(String.format("User with id %d can not add a product to store with id %d.", userId, storeId));
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        int newProductId = ProductController.getInstance().addProduct(storeId, productName, productQuantity, productPrice);
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
        ProductController.getInstance().removeProduct(storeId, productId);
        return productId;
    }

    public int updateProduct(int userId, int storeId, int productId, String newProductName, int newQuantity, int newPrice) {
        if(!UserController.getInstance().canUpdateProductsInStore(userId, storeId))
            throw new IllegalArgumentException(String.format("User with id %d can not update a product in store with id %d.", userId, storeId));
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!stores.get(storeId).productExists(productId))
            throw new IllegalArgumentException(String.format("A store with id %d does not have a product with id %d.", storeId, productId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        ProductController.getInstance().updateProduct(storeId, productId, newProductName, newQuantity, newPrice);
        return productId;
    }

    public void sendStoreOwnerRequest(int currentOwnerId, int newOwnerId, int storeId) {
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
        if(!stores.get(storeId).isStoreOwner(currentOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d.", currentOwnerId, storeId));
        if(stores.get(storeId).isStoreOwner(newOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d is already an owner of store %d.", newOwnerId, storeId));

        UserController.getInstance().sendStoreOwnerRequest(currentOwnerId, newOwnerId, storeId);
    }

    public void sendStoreManagerRequest(int currentOwnerId, int newManagerId, int storeId, Set<Integer> permissions) {
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));
        if(!stores.get(storeId).isStoreManager(currentOwnerId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d.", currentOwnerId, storeId));
        if(stores.get(storeId).isStoreManager(newManagerId))
            throw new IllegalArgumentException(String.format("A user with id %d is already a manager of store %d.", newManagerId, storeId));

        UserController.getInstance().sendStoreManagerRequest(currentOwnerId, newManagerId, storeId, permissions);
    }

    public void addStoreOwner(int newOwnerId, int storeId) {
        stores.get(storeId).addStoreOwner(newOwnerId);
    }

    public void addStoreManager(int newManagerId, int storeId) {
        stores.get(storeId).addStoreManager(newManagerId);
    }

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

    public List<UserDTO> getOwners(int userId, int storeId) {
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!stores.get(storeId).isStoreOwner(userId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d and can not request roles information.", userId, storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<Integer> ownerIds = stores.get(storeId).getOwnerIds();
        List<UserDTO> owners = new ArrayList<>();
        for(int ownerId : ownerIds) {
            owners.add(UserController.getInstance().getUser(ownerId));
        }
        return owners;
    }

    public List<UserDTO> getManagers(int userId, int storeId) {
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!stores.get(storeId).isStoreOwner(userId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d and can not request roles information.", userId, storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<Integer> managerIds = stores.get(storeId).getManagerIds();
        List<UserDTO> managers = new ArrayList<>();
        for(int managerId : managerIds) {
            managers.add(UserController.getInstance().getUser(managerId));
        }
        return managers;
    }

    public List<UserDTO> getSellers(int userId, int storeId) {
        if(!storeIdExists(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d does not exist.", storeId));
        if(!stores.get(storeId).isStoreOwner(userId))
            throw new IllegalArgumentException(String.format("A user with id %d is not an owner of store %d and can not request roles information.", userId, storeId));
        if(!isStoreActive(storeId))
            throw new IllegalArgumentException(String.format("A store with id %d is not active.", storeId));

        List<Integer> sellerIds = stores.get(storeId).getSellerIds();
        List<UserDTO> sellers = new ArrayList<>();
        for(int sellerId : sellerIds) {
            sellers.add(UserController.getInstance().getUser(sellerId));
        }
        return sellers;
    }



}
