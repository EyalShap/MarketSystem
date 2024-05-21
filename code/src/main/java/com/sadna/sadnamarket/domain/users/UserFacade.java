package com.sadna.sadnamarket.domain.users;

import com.sadna.sadnamarket.domain.stores.StoreFacade;

import java.util.HashSet;
import java.util.Set;

public class UserFacade {

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

    public void notify(int userId, String msg) {
        // Dana added this proxy function for the close store use case
    }

    public UserDTO getUser(int userId) {
        // Dana added this proxy function for the info request use case
        return new UserDTO();
    }

    public void sendStoreOwnerRequest(int currentOwnerId, int newOwnerId, int storeId) {
        // Dana added this proxy function for the add store owner use case
        // Assuming this function checks if currentOwnerId can assign newOwnerId as owner to storeId
    }

    public void sendStoreManagerRequest(int currentOwnerId, int newManagerId, int storeId, Set<Integer> permissions) {
        // Dana added this proxy function for the add store manager use case
        // Assuming this function checks if currentOwnerId can assign newManagerId as manager to storeId
        Set<ManagerPermission> convertedPermissions = convertPermissions(permissions);
    }

    private Set<ManagerPermission> convertPermissions(Set<Integer> managerPermissions) {
        Set<ManagerPermission> res = new HashSet<>();
        for(int permission : managerPermissions) {
            res.add(ManagerPermission.getPermission(permission));
        }
        return res;
    }

    public void acceptStoreOwnerRequest(int newOwnerId, int storeId) {
        // Dana added this proxy function for the add store owner use case
        // This function should add all of the roles and stuff
        //StoreFacade.getInstance().addStoreOwner(newOwnerId, storeId);
    }

    public void acceptStoreManagerRequest(int newManagerId, int storeId) {
        // Dana added this proxy function for the add store owner use case
        // This function should add all of the roles and stuff
        // The permissions should be in the NewManagerRequest object in the list of the user notifications / requests
        //StoreFacade.getInstance().addStoreManager(newManagerId, storeId);
    }

    public boolean canAddSellerToStore(int storeId, int userId) {
        // Dana added this proxy function
        return true;
    }

    public boolean canAddBuyPolicy(int storeId, int userId) {
        // Dana added this proxy function
        return true;
    }

    public boolean canAddDiscountPolicy(int storeId, int userId) {
        // Dana added this proxy function
        return true;
    }

    public boolean userExists(int userId) {
        // Dana added this proxy function
        return true;
    }

}
