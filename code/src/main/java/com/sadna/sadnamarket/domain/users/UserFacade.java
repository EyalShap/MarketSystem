package com.sadna.sadnamarket.domain.users;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;


//this is the facade of the Users package
//it merely exists so the folder appears on git
//you may delete these comments when beggining work
//have fun :)
public class UserFacade {
    private IUserRepository iUserRepo; 
    private static String systemManagerUserName;
    public static int guestId=1;

    private UserFacade(IUserRepository userRepo) {
        this.iUserRepo=userRepo;
        systemManagerUserName=null;
    }

    
    
    public synchronized int enterAsGuest(){
        guests.put(guestId,new Guest(guestId));
        guestId++;
        return guestId;
    }
    public synchronized void exitGuest(int guestId){
        guests.remove(guestId);
    }

    public boolean checkPremssionToStore(String userName, int storeId,Permission permission){
        Member member=iUserRepo.getMember(userName);
        member.hasPermissionToRole(permission,storeId);
        return false;
    }

    public void notify(String userName, String msg) {
        iUserRepo.getMember(userName).addNotification(msg);
    }

    public void setSystemManagerUserName(String username){
        systemManagerUserName=username;
    }
    public String setSystemManagerUserName(){
        return systemManagerUserName;
    }
    public void login(String userName,String password){//the cart of the guest
        validateAuth(userName, password);
        iUserRepo.getMember(userName).setLogin(true);
    }

    public void login(String userName,String password, int guestId){//the cart of the guest
        validateAuth(userName, password);
        Member member=iUserRepo.getMember(userName);
        member.setLogin(true);
        member.setCart(guests.get(guestId).getCart());
        guests.remove(guestId);
    }
    

    
    public int logout(String userName){//the cart of the guest
        if(!iUserRepo.hasMember(userName))
            throw new NoSuchElementException("User doesnt exist in system");

        iUserRepo.getMember(userName).logout();
        return enterAsGuest();
    }
    public void setCart(Cart cart,String userName){
        iUserRepo.getMember(userName).setCart(cart);
    }
  

    public void register(String userName, String Password){
        if(hasUser(userName)){
            throw new IllegalArgumentException("This name already in use");
        }
        Member member=new Member(userName);
        iUserRepo.store(member);
        userNameAndPassword.put(userName, Password);
    }

   
    public void addStoreManager(String username,int storeId){
        iUserRepo.getMember(username).addRole(new StoreManager(storeId));
    }
    public void addStoreOwner(String username,int storeId){
        iUserRepo.getMember(username).addRole(new StoreOwner(storeId));
    }
   
    public void addPremssionToStore(String userName, int storeId,Permission permission){
        Member member=iUserRepo.getMember(userName);
        member.addPermissionToRole(permission, storeId);
    }

    public void removeRole(String name,int storeId){
        Member member=iUserRepo.getMember(name);
        List<UserRole> roles=member.getUserRoles();
        for(UserRole role : roles){
           // role
        }

        // for (String username: appointments) {
        //     UserFacade.getInstance().removeRole(username,storeId);
        // }
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
