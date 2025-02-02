package com.sadna.sadnamarket.domain.users;

import java.time.LocalDate;
import java.util.List;

public interface IUserRepository {
    void store(String username,String firstName, String lastName,String emailAddress,String phoneNumber, LocalDate birthDate);
    boolean hasMember(String name);
    int addGuest();
    void deleteGuest(int guestID);
    List<CartItemDTO> getUserCart(String username);
    List<CartItemDTO> getGuestCart(int guestID);
    boolean hasPermissionToRole(String userName, Permission permission, int storeId);
    NotificationDTO addNotification(String userName, String msg);
    boolean isLoggedIn(String username);
    void setLogin(String userName, boolean b);
    void addProductToCart(String username, int storeId, int productId, int amount);
    void addProductToCart(int guestId, int storeId, int productId, int amount);
    void removeProductFromCart(String username, int storeId, int productId);
    void removeProductFromGuestCart(int guestId, int storeId, int productId);
    void changeQuantityCart(String username, int storeId, int productId, int amount);
    void guestChangeQuantityCart(int guestId, int storeId, int productId, int amount);
    void logout(String userName);
    void setCart(String userName, List<CartItemDTO> cart);
    void addRole(String username, StoreManager storeManager);
    void addRole(String username, StoreOwner storeOwner);
    void addPermissionToRole(String userName, Permission permission, int storeId);
    void removePermissionFromRole(String userName, Permission permission, int storeId);
    List<Permission> getPermissions(String userName, int storeId);
    void setFirstName(String userName, String firstName);
    void setLastName(String userName, String lastName);
    void setEmailAddress(String userName, String emailAddress);
    void setPhoneNumber(String userName, String phoneNumber);
    void setBirthday(String username,LocalDate birthDate);
    MemberDTO getMemberDTO(String userName);
    List<Integer> getOrdersHistory(String username);
    void clearCart(String username);
    List<NotificationDTO> getNotifications(String username);
    void addOrder(String username, int orderId);
    List<UserRoleDTO> getUserRolesString(String userName);
    UserRole getRoleOfStore(String userName, int storeId);
    List<UserRoleHibernate> getUserRoles(String username);
    RequestDTO addOwnerRequest(String senderName, UserFacade userFacade, String userName, int store_id);
    RequestDTO addManagerRequest(String senderName, UserFacade userFacade, String userName, int store_id);
    Request getRequest(String acceptingName, int requestID);
    void accept(String acceptingName, int requestID,UserFacade userFacade);
    void addApointer(String apointer, String acceptingName, int storeId);
    void reject(String rejectingName, int requestID);
    boolean isApointee(String giverUserName,String userName, int storeId);
    void leaveRole(String username, int storeId,UserFacade userFacade);
    void removeRoleFromMember(String username,String remover,  int storeId,UserFacade userFacade);
    RequestDTO addRequest(String senderName, String sentName,int storeId, String reqType);
    void clearGuestCart(int guestID);
    void clear();
    StoreManager createStoreManagerRole(int storeId);
    StoreOwner createStoreOwnerRole(int storeId,String apointee);
    StoreFounder createStoreFounderRole(int storeId, String apointee);
    boolean isGuestExist(int guestID);
    void logoutMembers();
    void removeGuests();
}
