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
    public static int guestId=1;

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
    
    public synchronized int enterAsGuest(){
        guests.put(guestId,new Guest(guestId));
        guestId++;
        return guestId;
    }
    public synchronized void exitGuest(int guestId){
        guests.remove(guestId);
    }
    private boolean checkPremssionToStore(String userName, int storeId,Permission permission){
        Member member=getMember(userName);
        member.hasPermissionToRole(permission,storeId);
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

    public void setSystemManagerUserName(String username){
        systemManagerUserName=username;
    }
    public String setSystemManagerUserName(){
        return systemManagerUserName;
    }
    public void login(String userName,String password){//the cart of the guest
        validateAuth(userName, password);
        getMember(userName).setLogin(true);
    }

    public void login(String userName,String password, int guestId){//the cart of the guest
        validateAuth(userName, password);
        Member member=getMember(userName);
        member.setLogin(true);
        member.setCart(guests.get(guestId).getCart());
        guests.remove(guestId);
    }
    private boolean isPasswordCorrect(String userName,String password){
        return userNameAndPassword.get(userName).equals(password);
    }

    private void validateAuth(String userName,String password){
        if(!hasUser(userName))
            throw new NoSuchElementException("User doesnt exist in system");
        if(!isPasswordCorrect(userName,password))
            throw new NoSuchElementException("User doesnt exist in system");
    }
    public int logout(String userName){//the cart of the guest
        if(!hasUser(userName))
            throw new NoSuchElementException("User doesnt exist in system");

        getMember(userName).logout();
        return enterAsGuest();
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
        Member member=new Member();
        members.put(userName, member);
        userNameAndPassword.put(userName, Password);
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
    private void addPremssionToStore(String userName, int storeId,Permission permission){
        Member member=getMember(userName);
        member.addPermissionToRole(permission, storeId);
    }
    public void AddPermissionAddProductsToStore(String userName, int storeId) {
        addPremssionToStore(userName,storeId,Permission.ADD_PRODUCTS);
    }

    public void AddPermissionDeleteProductsFromStore(String userName, int storeId) {
        addPremssionToStore(userName,storeId,Permission.DELETE_PRODUCTS);
    }

    public void AddPermissionUpdateProductsInStore(String userName, int storeId) {
        addPremssionToStore(userName,storeId,Permission.UPDATE_PRODUCTS);
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
