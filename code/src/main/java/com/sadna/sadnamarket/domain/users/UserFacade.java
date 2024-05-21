package com.sadna.sadnamarket.domain.users;
import java.util.List;
import java.util.NoSuchElementException;



public class UserFacade {
    private IUserRepository iUserRepo; 
    private static String systemManagerUserName;

    private UserFacade(IUserRepository userRepo) {
        this.iUserRepo=userRepo;
        systemManagerUserName=null;
    }
    
    public synchronized int enterAsGuest(){
       return iUserRepo.addGuest();
    }

    public synchronized void exitGuest(int guestId){
        iUserRepo.deleteGuest(guestId);
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
    public void login(String userName,String password){
        iUserRepo.getMember(userName).setLogin(true);
    }

    public void login(String userName,String password, int guestId){//the cart of the guest
        Member member=iUserRepo.getMember(userName);
        if(member.getCart().isEmpty()){
            member.setCart(iUserRepo.getGuest(guestId).getCart());
        }
        member.setLogin(true);
        iUserRepo.deleteGuest(guestId);
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
        Member member=new Member(userName);
        iUserRepo.store(member);
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
