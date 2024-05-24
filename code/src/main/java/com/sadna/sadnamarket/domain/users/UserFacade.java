package com.sadna.sadnamarket.domain.users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.stores.IStoreRepository;
import com.sadna.sadnamarket.domain.stores.StoreFacade;



public class UserFacade {
    private IUserRepository iUserRepo; 
    private static String systemManagerUserName;
    private static StoreFacade storeFacade;

    public UserFacade(IUserRepository userRepo, IStoreRepository iStoreRepository) {
        this.iUserRepo=userRepo;
        systemManagerUserName=null;
        storeFacade=new StoreFacade(iStoreRepository);
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
        List<Notification> notifes = new ArrayList<>(iUserRepo.getMember(userName).getNotifications().values());
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
    public void addProductToCart(String username,int storeId, int productId,int amount){
        if(amount<=0)
            throw new IllegalArgumentException("amount should be above 0");
        iUserRepo.getMember(username).addProductToCart(storeId, productId, amount);
    }
    public void addProductToCart(int guestId,int storeId, int productId,int amount){
        if(amount<=0)
            throw new IllegalArgumentException("amount should be above 0");
        iUserRepo.getGuest(guestId).addProductToCart(storeId, productId, amount);
    }
    public void removeProductFromCart(String username,int storeId, int productId){
        iUserRepo.getMember(username).removeProductFromCart(storeId, productId);
    }
    public void removeProductFromCart(int guestId,int storeId, int productId){
        iUserRepo.getGuest(guestId).removeProductFromCart(storeId, productId);
    }
    public void changeQuantityCart(String username,int storeId, int productId,int amount){
        if(amount<=0)
            throw new IllegalArgumentException("amount should be above 0");
        iUserRepo.getMember(username).changeQuantityCart(storeId, productId, amount);
    }
    public void changeQuantityCart(int guestId,int storeId, int productId,int amount){
        if(amount<=0)
            throw new IllegalArgumentException("amount should be above 0");
        iUserRepo.getGuest(guestId).changeQuantityCart(storeId, productId, amount);
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

    public void accept(String acceptingName,int requestID){
        Member accepting=getMember(acceptingName);
        Request request=accepting.getRequest(requestID);
        int storeId=request.getStoreId();
        accepting.accept(requestID);
        String role=request.getRole();
        if(role.equals("manager"))
            storeFacade.addStoreManager(acceptingName, storeId);
        else
            storeFacade.addStoreOwner(acceptingName,storeId);
    }


    public void login(String userName,String password, int guestId){//the cart of the guest
        isValid(userName);
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
  

    public void register(String username,String firstName, String lastName,String emailAddress,String phoneNumber){
        Member member=new Member(username,firstName,lastName,emailAddress,phoneNumber);
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
    public void removePremssionFromStore(String userName, int storeId,Permission permission){
        Member member=iUserRepo.getMember(userName);
        member.removePermissionFromRole(permission, storeId);
    }

    public void leaveRole(String name,int storeId){
        Member member=iUserRepo.getMember(name);
        List<UserRole> roles=member.getUserRoles();
        for(UserRole role : roles){
           // role
           if(role.getStoreId()==storeId){
            role.leaveRole(new UserRoleVisitor(), storeId, member,this);;
           }
        }
    }
    public void removeRoleFromMember(String username,String remover,int storeId){
        Member member=iUserRepo.getMember(username);
        List<UserRole> roles=member.getUserRoles();
        for(UserRole role : roles){
           // role
           if(role.getStoreId()==storeId){
            if(!role.getApointee().equals(remover))
                throw new IllegalStateException("you can only remove your apointees");
            role.leaveRole(new UserRoleVisitor(), storeId, member,this);;
           }
        }
    }
    public void setFirstName(String userName, String firstName) {
        isValid(firstName);
        Member member = iUserRepo.getMember(userName);
        member.setFirstName(firstName);
    }

    public void setLastName(String userName, String lastName) {
        isValid(lastName);
        Member member = iUserRepo.getMember(userName);
        member.setLastName(lastName);
    }

    public void setEmailAddress(String userName, String emailAddress) {
        isValid(emailAddress);
        Member member = iUserRepo.getMember(userName);
        member.setEmailAddress(emailAddress);
    }

    public void setPhoneNumber(String userName, String phoneNumber) {
        isValid(phoneNumber);
        Member member = iUserRepo.getMember(userName);
        member.setPhoneNumber(phoneNumber);
    }
    private void isValid(String detail){
        if(detail==null||detail.trim().equals(""))
            throw new IllegalArgumentException("please enter valid string");
    }
    public MemberDTO getMemberDTO(String userName){
        isValid(userName);
        Member member=iUserRepo.getMember(userName);
        MemberDTO memberDTO=new MemberDTO(member);
        return memberDTO;
    }
    public List<Integer> getMemberPermissions(String userName, int storeId){
        isValid(userName);
        UserRole role=iUserRepo.getMember(userName).getRoleOfStore(storeId);
        List<Integer> permissionsInRole=new ArrayList<>();
        for(Permission permission: Permission.values()){
            if (role.hasPermission(permission))
                permissionsInRole.add(permission.getValue());

        }
        return permissionsInRole;

    }

    public HashMap<Integer,OrderDTO> getUserOrders(String username){
        List<Integer> orders=getMember(username).getOrdersHistory();
        for (Integer orderId : orders) {
           //getOrderById(orderId);
        }
        return null;
    }
    public void viewCart(String username){
        // call check validation from store
        // getPrice before and after discount
    }
    public void purchaseCart(String username){
        // call check validation from store
        // getPrice before and after discount
        // proxy payment
        // update quantities
        // create new order 
    }

}
