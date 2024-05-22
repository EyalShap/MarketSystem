package com.sadna.sadnamarket.domain.users;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;



public class UserFacade {
    private IUserRepository iUserRepo; 
    private static String systemManagerUserName;

    public UserFacade(IUserRepository userRepo) {
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

    public List<NotificationDTO> getNotifications(String userName){
        List<Notification> notifes= (ArrayList) iUserRepo.getMember(userName).getNotifications().values();
        List<NotificationDTO> notificationDTOs=new ArrayList<NotificationDTO>();
        for(Notification notif : notifes){
            notificationDTOs.add(new NotificationDTO(notif));
        }
        return notificationDTOs;
    }


    public boolean isLoggedIn(String userName){
        if(isExist(userName)){
            Member member= iUserRepo.getMember(userName);
            return member.isLoggedIn();
        }
        return false;
    }

    public boolean isExist(String userName){
        return iUserRepo.hasMember(userName);
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

    
    public Member getMember(String userName){
        return iUserRepo.getMember(userName);
    }    
    
    public void addOwnerRequest(String senderName,String userName,int store_id){
        Member sender=getMember(senderName);
        sender.addOwnerRequest(this,userName, store_id);
    }

     public void addManagerRequest(String senderName,String userName,int store_id){
        Member sender=getMember(senderName);
        sender.addManagerRequest(this,userName, store_id);
    }

    public void acceptOwnerRequest(String acceptingName,int requestID){
        Member accepting=getMember(acceptingName);
        accepting.acceptToOwner(requestID);
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
    public void addStoreOwner(String username,String asignee, int storeId){
        iUserRepo.getMember(username).addRole(new StoreOwner(storeId,asignee));
    }
    public void addStoreFounder(String username,int storeId){
        iUserRepo.getMember(username).addRole(new StoreFounder(storeId,username));
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
           if(role.getStoreId()==storeId){
            role.leaveRole(new UserRoleVisitor(), storeId, member,this);;
           }
        }
    }

}
