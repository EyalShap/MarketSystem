package com.sadna.sadnamarket.domain.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Member extends IUser {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private List<UserRole> roles;
    private List<Integer> orders;
    private HashMap<Integer, Notification> notifes;
    private static final Logger logger = LogManager.getLogger(Member.class);
    private boolean isLoggedIn;
    private int notifyID;

    public Member(String username, String firstName, String lastName, String emailAddress, String phoneNumber) {
        logger.info("Entering Member constructor with parameters: username={}, firstName={}, lastName={}, emailAddress={}, phoneNumber={}",
                username, firstName, lastName, emailAddress, phoneNumber);
        roles = new ArrayList<>();
        notifes = new HashMap<>();
        orders = new ArrayList<>();
        isLoggedIn = false;
        notifyID = 0;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        logger.info("Exiting Member constructor");
    }

    @Override
    public boolean isLoggedIn() {
        logger.info("Entering isLoggedIn");
        boolean result = isLoggedIn;
        logger.info("Exiting isLoggedIn with result={}", result);
        return result;
    }

    public void setCart(Cart cart) {
        logger.info("Entering setCart with cart={}", cart);
        if (this.cart.isEmpty())
            this.cart = cart;
        logger.info("Exiting setCart");
    }

    public void setLogin(boolean isLoggedIn) {
        logger.info("Entering setLogin with isLoggedIn={}", isLoggedIn);
        this.isLoggedIn = isLoggedIn;
        logger.info("Exiting setLogin");
    }

    public void addNotification(String message) {
        logger.info("Entering addNotification with message={}", message);
        notifes.put(++notifyID, new Notification(message,notifyID));
        logger.info("Exiting addNotification");
    }

    public void addOwnerRequest(UserFacade userFacade, String userName, int store_id) {
        logger.info("Entering addOwnerRequest with userFacade={}, userName={}, store_id={}", userFacade, userName, store_id);
        UserRole role = getRoleOfStore(store_id);
        if (role.getApointee().equals(userName)) {
            logger.error("Exception in addOwnerRequest: You disallowed appoint the one who appointed you!");
            throw new IllegalStateException("You disallowed appoint the one who appointed you!");
        }
        role.sendRequest(userFacade, username, userName, "Owner");
        logger.info("Exiting addOwnerRequest");
    }

    public void addManagerRequest(UserFacade userFacade, String userName, int store_id) {
        logger.info("Entering addManagerRequest with userFacade={}, userName={}, store_id={}", userFacade, userName, store_id);
        UserRole role = getRoleOfStore(store_id);
        role.sendRequest(userFacade, username, userName, "Manager");
        logger.info("Exiting addManagerRequest");
    }

    public UserRole getRoleOfStore(int store_id) {
        logger.info("Entering getRoleOfStore with store_id={}", store_id);
        for (UserRole role : getUserRoles()) {
            if (role.getStoreId() == store_id) {
                logger.info("Exiting getRoleOfStore with result={}", role);
                return role;
            }
        }
        logger.error("Exception in getRoleOfStore: User has no role in this store");
        throw new IllegalArgumentException("User has no role in this store");
    }

    private boolean hasRoleInStore(int store_id) {
        logger.info("Entering hasRoleInStore with store_id={}", store_id);
        for (UserRole role : getUserRoles()) {
            if (role.getStoreId() == store_id) {
                logger.info("Exiting hasRoleInStore with result=true");
                return true;
            }
        }
        logger.info("Exiting hasRoleInStore with result=false");
        return false;
    }

    public void logout() {
        logger.info("Entering logout");
        if (!isLoggedIn) {
            logger.error("Exception in logout: user isn't logged in");
            throw new IllegalStateException("user isn't logged in");
        }
        this.setLogin(false);
        logger.info("Exiting logout");
    }

    
    public void addRole(UserRole role) {
        logger.info("Entering addRole with role={}", role);
        roles.add(role);
        logger.info("Exiting addRole");
    }

    public void removeRole(UserRole role) {
        logger.info("Entering removeRole with role={}", role);
        roles.remove(role);
        logger.info("Exiting removeRole");
    }

    public void addApointer(String apointed, int storeId) {
        logger.info("Entering addApointer with apointed={}, storeId={}", apointed, storeId);
        getRoleOfStore(storeId).addAppointers(apointed);
        logger.info("Exiting addApointer");
    }

    public void addPermissionToRole(Permission permission, int storeId) {
        logger.info("Entering addPermissionToRole with permission={}, storeId={}", permission, storeId);
        for (UserRole role : getUserRoles()) {
            if (role.getStoreId() == storeId) {
                role.addPermission(permission);
            }
        }
        logger.info("Exiting addPermissionToRole");
    }

    public boolean hasPermissionToRole(Permission permission, int storeId) {
        logger.info("Entering hasPermissionToRole with permission={}, storeId={}", permission, storeId);
        for (UserRole role : getUserRoles()) {
            if (role.getStoreId() == storeId && role.hasPermission(permission)) {
                logger.info("Exiting hasPermissionToRole with result=true");
                return true;
            }
        }
        logger.info("Exiting hasPermissionToRole with result=false");
        return false;
    }

    public void removePermissionFromRole(Permission permission, int storeId) {
        logger.info("Entering removePermissionFromRole with permission={}, storeId={}", permission, storeId);
        for (UserRole role : getUserRoles()) {
            if (role.getStoreId() == storeId) {
                role.removePermission(permission);
            }
        }
        logger.info("Exiting removePermissionFromRole");
    }

    public HashMap<Integer, Notification> getNotifications() {
        logger.info("Entering getNotifications");
        HashMap<Integer, Notification> result = notifes;
        logger.info("Exiting getNotifications with result={}", result);
        return result;
    }

    public String getUsername() {
        logger.info("Entering getUsername");
        String result = username;
        logger.info("Exiting getUsername with result={}", result);
        return result;
    }

    public void getRequest(String senderName, int storeId, String reqType) {
        logger.info("Entering getRequest with senderName={}, storeId={}, reqType={}", senderName, storeId, reqType);
        if (hasRoleInStore(storeId)) {
            logger.error("Exception in getRequest: member already has role in store");
            throw new IllegalStateException("member already has role in store");
        }
        notifes.put(++notifyID, new Request(senderName, "You got appointment request", storeId, reqType,notifyID));
        logger.info("Exiting getRequest");
    }

    public void accept(int requestID) {
        logger.info("Entering accept with requestID={}", requestID);
        notifes.get(requestID).accept(this);
        notifes.remove(requestID);
        logger.info("Exiting accept");
    }



    public Request getRequest(int request_id) {
        logger.info("Entering getRequest with request_id={}", request_id);
        Request result = (Request) notifes.get(request_id);
        logger.info("Exiting getRequest with result={}", result);
        return result;
    }

    // Getter for firstName
    public String getFirstName() {
        logger.info("Entering getFirstName");
        String result = firstName;
        logger.info("Exiting getFirstName with result={}", result);
        return result;
    }

    // Setter for firstName
    public void setFirstName(String firstName) {
        logger.info("Entering setFirstName with firstName={}", firstName);
        this.firstName = firstName;
        logger.info("Exiting setFirstName");
    }

    // Getter for lastName
    public String getLastName() {
        logger.info("Entering getLastName");
        String result = lastName;
        logger.info("Exiting getLastName with result={}", result);
        return result;
    }

    // Setter for lastName
    public void setLastName(String lastName) {
        logger.info("Entering setLastName with lastName={}", lastName);
        this.lastName = lastName;
        logger.info("Exiting setLastName");
    }

    // Getter for emailAddress
    public String getEmailAddress() {
        logger.info("Entering getEmailAddress");
        String result = emailAddress;
        logger.info("Exiting getEmailAddress with result={}", result);
        return result;
    }

    // Setter for emailAddress
    public void setEmailAddress(String emailAddress) {
        logger.info("Entering setEmailAddress with emailAddress={}", emailAddress);
        this.emailAddress = emailAddress;
        logger.info("Exiting setEmailAddress");
    }

    // Getter for phoneNumber
    public String getPhoneNumber() {
        logger.info("Entering getPhoneNumber");
        String result = phoneNumber;
        logger.info("Exiting getPhoneNumber with result={}", result);
        return result;
    }

    // Setter for phoneNumber
    public void setPhoneNumber(String phoneNumber) {
        logger.info("Entering setPhoneNumber with phoneNumber={}", phoneNumber);
        this.phoneNumber = phoneNumber;
        logger.info("Exiting setPhoneNumber");
    }

    public List<Integer> getOrdersHistory() {
        logger.info("Entering getOrdersHistory");
        List<Integer> result = orders;
        logger.info("Exiting getOrdersHistory with result={}", result);
        return result;
    }

    public List<String> getUserRolesString() {
        logger.info("Entering getUserRolesString");
        List<String> rolesString = new ArrayList<>();
        for (UserRole role : roles) {
            rolesString.add(role.toString());
        }
        logger.info("Exiting getUserRolesString with result={}", rolesString);
        return rolesString;
    }

    public List<UserRole> getUserRoles() {
        logger.info("Entering getUserRoles");
        List<UserRole> result = roles;
        logger.info("Exiting getUserRoles with result={}", result);
        return result;
    }
}
