package com.sadna.sadnamarket.domain.users;

import java.util.HashMap;
import java.util.NoSuchElementException;
;


//this is the facade of the Users package
//it merely exists so the folder appears on git
//you may delete these comments when beggining work
//have fun :)
public class UserController {
    private static UserController instance;
    private static HashMap<String,Member> members;
    private static HashMap<Integer,Guest> guests;
    private static HashMap<String,String> userNameAndPassword;
    private static String systemManagerUserName;
    public static int guestId;

    private UserController() {
        members=new HashMap<>();
        userNameAndPassword=new HashMap<>();
        systemManagerUserName=null;
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }  
    
    public synchronized void enterAsGuest(){
        guests.put(guestId,new Guest());
        guestId++;
    }

    public boolean checkPremssionToStore(String userName, int storeId,Permission permission){
        Member member=getMember(userName);
        for(UserRole role: member.getUserRoles()){
            if(role.getStoreId()==storeId && role.hasPermission(permission)){
                return true;
            }
        }
        return false;
    }

    public boolean canAddProductsToStore(String userName, int storeId) {
        return checkPremssionToStore(userName,storeId,Permission.ADD_PRODUCTS);
    }

    public boolean canDeleteProductsFromStore(String userName, int storeId) {
        return checkPremssionToStore(userName,storeId,Permission.DELETE_PRODUCTS);
    }

    public boolean canUpdateProductsInStore(String userName, int storeId) {
        return checkPremssionToStore(userName,storeId,Permission.UPDATE_PRODUCTS);
    }

    public void notify(String userName, String msg) {
        getMember(userName).addNotification(msg);
    }


    public void login(String userName,String password){//the cart of the guest
        if(!hasUser(userName))
            throw new NoSuchElementException("User doesnt exist in system");
        getMember(userName).setLogin(true);
    }
    public IUser logout(String userName){//the cart of the guest
        if(!hasUser(userName))
            throw new NoSuchElementException("User doesnt exist in system");

        getMember(userName).logout();
        Guest guest=new Guest();
        return guest;
    }
    public void setCart(Cart cart,String userName){
        getMember(userName).setCart(cart);
    }
    private boolean hasUser(String userName){
        return userNameAndPassword.containsKey(userName);
    }

    public void register(String userName, String Password){
        if(hasUser(userName)){
            throw new IllegalArgumentException("This name already in use");
        }

    }

    public Member getMember(String userName){
        if(!hasUser(userName))
            throw new NoSuchElementException("User doesnt exist in system");
        return members.get(userName);
    }
    public void addStoreManager(String username,int storeId){
        members.get(username).addRole(new StoreManager(storeId));
    }
    public void addStoreOwner(String username,int storeId){
        members.get(username).addRole(new StoreOwner(storeId));
    }
    public void removeRole(String username,int storeId){
        members.get(username).removeRole(storeId);
    }
    /*
    public void addStoreOwnerRole(int currentOwnerId, int newOwnerId, int storeId) {
        // Dana added this proxy function for adding a new owner use case
        // Assuming this function checks if currentOwnerId can assign new owners to storeId
    }

    public void addStoreManagerRole(int currentOwnerId, int newManagerId, int storeId, Set<Integer> managerPermissions) {
        // Dana added this proxy function for adding a new manager use case
        // Assuming this function checks if currentOwnerId can assign managers to storeId
    }

    private Set<ManagerPermission> getPermissions(Set<Integer> managerPermissions) {
        Set<ManagerPermission> res = new HashSet<>();
        for(int permission : managerPermissions) {
            res.add(ManagerPermission.getPermission(permission));
        }
        return res;
    }*/
}
