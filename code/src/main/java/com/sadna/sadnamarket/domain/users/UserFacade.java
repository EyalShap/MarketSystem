package com.sadna.sadnamarket.domain.users;

import java.util.HashSet;
import java.util.Set;

public class UserFacade {

    public boolean checkPremssionToStore(String userName, int storeId, Permission permission){
        //Member member=iUserRepo.getMember(userName);
        //member.hasPermissionToRole(permission,storeId);
        //return false;
        return true;
    }

    public boolean isExist(String userName){
        //return iUserRepo.hasMember(userName);
        return true;
    }

    public void notify(String userName, String msg) {
        //iUserRepo.getMember(userName).addNotification(msg);
    }

    public UserDTO getUser(String username) {
        return new UserDTO();
    }

    public void sendStoreOwnerRequest(String currentOwnerUsername, String newOwnerUsername, int storeId) {
        // Dana added this proxy function for the add store owner use case
        // Assuming this function checks if currentOwnerId can assign newOwnerId as owner to storeId
    }

    public void sendStoreManagerRequest(String currentOwnerUsername, String newManagerUsername, int storeId, Set<Integer> permissions) {
        // Dana added this proxy function for the add store manager use case
        // Assuming this function checks if currentOwnerId can assign newManagerId as manager to storeId
        //Set<Permission> convertedPermissions = convertPermissions(permissions);
    }

    public void acceptStoreOwnerRequest(String newOwnerUsername, int storeId) {
        // Dana added this proxy function for the add store owner use case
        // This function should add all of the roles and stuff
        //StoreFacade.getInstance().addStoreOwner(newOwnerId, storeId);
    }

    public void acceptStoreManagerRequest(String newManagerUsername, int storeId) {
        // Dana added this proxy function for the add store owner use case
        // This function should add all of the roles and stuff
        // The permissions should be in the NewManagerRequest object in the list of the user notifications / requests
        //StoreFacade.getInstance().addStoreManager(newManagerId, storeId);
    }
}
