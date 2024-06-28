package com.sadna.sadnamarket.domain.users;

import java.time.LocalDate;
import java.util.List;

public class UserHibernateRepo implements IUserRepository {


    @Override
    public List<Member> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public void store(Member member) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'store'");
    }

    @Override
    public boolean hasMember(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasMember'");
    }

    @Override
    public int addGuest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addGuest'");
    }

    @Override
    public void deleteGuest(int guestID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteGuest'");
    }

    @Override
    public Guest getGuest(int guestID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGuest'");
    }

    @Override
    public List<CartItemDTO> getUserCart(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserCart'");
    }

    @Override
    public List<CartItemDTO> getGuestCart(int guestID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGuestCart'");
    }

    @Override
    public boolean hasPermissionToRole(String userName, Permission permission, int storeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasPermissionToRole'");
    }

    @Override
    public NotificationDTO addNotification(String userName, String msg) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNotification'");
    }

    @Override
    public boolean isLoggedIn(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isLoggedIn'");
    }

    @Override
    public void setLogin(String userName, boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLogin'");
    }

    @Override
    public void addProductToCart(String username, int storeId, int productId, int amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addProductToCart'");
    }

    @Override
    public void removeProductFromCart(String username, int storeId, int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeProductFromCart'");
    }

    @Override
    public void removeProductFromGuestCart(int guestId, int storeId, int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeProductFromGuestCart'");
    }

    @Override
    public void changeQuantityCart(String username, int storeId, int productId, int amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeQuantityCart'");
    }

    @Override
    public void guestChangeQuantityCart(int guestId, int storeId, int productId, int amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guestChangeQuantityCart'");
    }

    @Override
    public void logout(String userName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    @Override
    public void setCart(String userName, Cart cart) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCart'");
    }

    @Override
    public void addRole(String username, StoreManager storeManager) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addRole'");
    }

    @Override
    public void addRole(String username, StoreOwner storeOwner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addRole'");
    }

    @Override
    public void addPermissionToRole(String userName, Permission permission, int storeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPermissionToRole'");
    }

    @Override
    public void removePermissionFromRole(String userName, Permission permission, int storeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePermissionFromRole'");
    }

    @Override
    public List<Permission> getPermissions(String userName, int storeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPermissions'");
    }

    @Override
    public void setFirstName(String userName, String firstName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFirstName'");
    }

    @Override
    public void setLastName(String userName, String lastName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLastName'");
    }

    @Override
    public void setEmailAddress(String userName, String emailAddress) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEmailAddress'");
    }

    @Override
    public void setPhoneNumber(String userName, String phoneNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPhoneNumber'");
    }

    @Override
    public void setBirthday(LocalDate birthDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBirthday'");
    }

    @Override
    public MemberDTO getMemberDTO(String userName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemberDTO'");
    }

    @Override
    public List<Integer> getOrdersHistory(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrdersHistory'");
    }

    @Override
    public void clearCart(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearCart'");
    }

    @Override
    public List<NotificationDTO> getNotifications(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNotifications'");
    }

    @Override
    public void addOrder(String username, int orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addOrder'");
    }

    @Override
    public List<UserRoleDTO> getUserRolesString(String userName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserRolesString'");
    }

    @Override
    public UserRole getRoleOfStore(String userName, int storeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoleOfStore'");
    }

    @Override
    public List<UserRole> getUserRoles(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserRoles'");
    }

    @Override
    public RequestDTO addOwnerRequest(String senderName, UserFacade userFacade, String userName, int store_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addOwnerRequest'");
    }

    @Override
    public RequestDTO addManagerRequest(String senderName, UserFacade userFacade, String userName, int store_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addManagerRequest'");
    }

    @Override
    public Request getRequest(String acceptingName, int requestID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequest'");
    }

    @Override
    public void accept(String acceptingName, int requestID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }

    @Override
    public void addApointer(String apointer, String acceptingName, int storeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addApointer'");
    }

    @Override
    public void reject(String rejectingName, int requestID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reject'");
    }

    @Override
    public void leaveRole(String username, int storeId, UserFacade userFacade) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveRole'");
    }

    @Override
    public void removeRoleFromMember(String username, String remover, int storeId, UserFacade userFacade) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeRoleFromMember'");
    }

    @Override
    public RequestDTO addRequest(String senderName, String sentName, int storeId, String reqType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addRequest'");
    }

    @Override
    public boolean isApointee(String giverUserName, String userName, int storeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isApointee'");
    }
    
}
