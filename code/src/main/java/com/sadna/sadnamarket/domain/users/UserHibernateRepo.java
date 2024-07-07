package com.sadna.sadnamarket.domain.users;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import jakarta.persistence.QueryHint;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sadna.sadnamarket.service.Error;

import com.sadna.sadnamarket.HibernateUtil;
import org.springframework.data.jpa.repository.QueryHints;

public class UserHibernateRepo implements IUserRepository {


    @Override
    public void store(String username,String firstName, String lastName,String emailAddress,String phoneNumber, LocalDate birthDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = new Member(username, firstName, lastName, emailAddress, phoneNumber,birthDate);
            session.save(member);
            transaction.commit();
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public boolean hasMember(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, name);
            if (member != null) {
                return true;
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(name));
            }
        }
    }

    @Override
    public int addGuest() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Guest guest = new Guest();
            session.save(guest);
            transaction.commit();
            return guest.guestId;
        } 
    }

    @Override
    public void deleteGuest(int guestID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Guest guest = session.get(Guest.class, guestID);
            if (guest != null) {
                session.delete(guest);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberGuestDoesntExistError(guestID));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<CartItemDTO> getUserCart(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, username);
            if (member != null) {
                return member.getCartItems();
            } else {
                return null;
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<CartItemDTO> getGuestCart(int guestID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Guest guest = session.get(Guest.class, guestID);
            if (guest != null) {
                return guest.getCartItems();
            } else {
                throw new NoSuchElementException(Error.makeMemberGuestDoesntExistError(guestID));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public boolean hasPermissionToRole(String userName, Permission permission, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, userName);
            if (member != null) {
                return member.hasPermissionToRole(permission, storeId);
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public NotificationDTO addNotification(String userName, String msg) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, userName);
            if (member != null) {
                Notification notif=new Notification(msg);
                session.save(notif);
                NotificationDTO notification=member.addNotification(notif);
                session.update(member);
                transaction.commit();
                return notification;
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public boolean isLoggedIn(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, username);
            return member != null && member.isLoggedIn();
        }
    }

    @Override
    public void setLogin(String userName, boolean b) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, userName);
            if (member != null) {
                member.setLogin(b);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void addProductToCart(String username, int storeId, int productId, int amount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.addProductToCart(storeId, productId, amount);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void removeProductFromCart(String username, int storeId, int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.removeProductFromCart(storeId, productId);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void removeProductFromGuestCart(int guestId, int storeId, int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Guest guest = session.get(Guest.class, guestId);
            if (guest != null) {
                guest.removeProductFromCart(storeId, productId);
                session.update(guest);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberGuestDoesntExistError(guestId));
            }
        }
    }

    @Override
    public void changeQuantityCart(String username, int storeId, int productId, int amount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.changeQuantityCart(storeId, productId, amount);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void guestChangeQuantityCart(int guestId, int storeId, int productId, int amount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Guest guest = session.get(Guest.class, guestId);
            if (guest != null) {
                guest.changeQuantityCart(storeId, productId, amount);
                session.update(guest);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberGuestDoesntExistError(guestId));
            }
        }
    }

    @Override
    public void logout(String userName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, userName);
            if (member != null) {
                member.logout();
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void setCart(String userName, List<CartItemDTO> cartLst) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, userName);
            if (member != null) {
                Cart cart = new Cart(cartLst);
                member.setCart(cart);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void addRole(String username, StoreManager storeManager) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.addRole(storeManager);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void addRole(String username, StoreOwner storeOwner) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.addRole(storeOwner);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void addPermissionToRole(String username, Permission permission, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.addPermissionToRole(permission, storeId);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void removePermissionFromRole(String username, Permission permission, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.removePermissionFromRole(permission, storeId);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<Permission> getPermissions(String userName, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, userName);
            if (member != null) {
                return member.getPermissions(storeId);
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void setFirstName(String userName, String firstName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, userName);
            if (member != null) {
                member.setFirstName(firstName);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void setLastName(String userName, String lastName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, userName);
            if (member != null) {
                member.setLastName(lastName);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void setEmailAddress(String userName, String emailAddress) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, userName);
            if (member != null) {
                member.setEmailAddress(emailAddress);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void setPhoneNumber(String userName, String phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, userName);
            if (member != null) {
                member.setPhoneNumber(phoneNumber);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void setBirthday(String username,LocalDate birthDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.setBirthday(birthDate);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public MemberDTO getMemberDTO(String userName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, userName);
            if (member != null) {
                return new MemberDTO(member);
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<Integer> getOrdersHistory(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, username);
            if (member != null) {
                return member.getOrdersHistory();
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void clearCart(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.clearCart();
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<NotificationDTO> getNotifications(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                return member.getNotifications().values().stream().map(Notification::toDTO).toList();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void addOrder(String username, int orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                member.addOrder(orderId);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<UserRoleDTO> getUserRolesString(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                return member.getUserRolesString();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public UserRole getRoleOfStore(String userName, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, userName);
            if (member != null) {
                return member.getRoleOfStore(storeId);
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<UserRoleHibernate> getUserRoles(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, username);
            if (member != null) {
                return member.getUserRoles();
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public RequestDTO addOwnerRequest(String senderName, UserFacade userFacade, String userName, int store_id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, senderName);
            if (member != null) {
                RequestDTO request = member.addOwnerRequest(userFacade, userName, store_id);
                return request;
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(senderName));
            }
        }
    }

    @Override
    public RequestDTO addManagerRequest(String senderName, UserFacade userFacade, String userName, int store_id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, senderName);
            if (member != null) {
                RequestDTO request = member.addManagerRequest(userFacade, userName, store_id);
                return request;
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(senderName));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public Request getRequest(String acceptingName, int requestID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, acceptingName);
            if (member != null) {
                return member.getRequest(requestID);
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(acceptingName));
            }
        }
    }

    @Override
    public void accept(String acceptingName, int requestID,UserFacade userFacade) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, acceptingName);
            if (member != null) {
                member.accept(requestID,userFacade);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(acceptingName));
            }
        }
    }

    @Override
    public void addApointer(String apointer, String acceptingName, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, apointer);
            if (member != null) {
                member.addApointer(acceptingName, storeId);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(apointer));
            }
        }
    }

    @Override
    public void reject(String rejectingName, int requestID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, rejectingName);
            if (member != null) {
                member.reject(requestID);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(rejectingName));
            }
        }
    }

    @Override
    public void leaveRole(String username, int storeId, UserFacade userFacade) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, username);
            if (member != null) {
                UserRole role = member.getRoleOfStore(storeId);
               role.leaveRole(new UserRoleVisitor(),storeId,member,userFacade);
                 session.delete(role);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void removeRoleFromMember(String username, String remover, int storeId, UserFacade userFacade) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, remover);
            if (member != null) {
                UserRole role = member.getRoleOfStore(storeId);
                member.removeRole(role);
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(remover));
            }
        }
    }

    @Override
    public RequestDTO addRequest(String senderName, String sentName, int storeId, String reqType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Member member = session.get(Member.class, sentName);
            if (member != null) {
                Request req=new Request(senderName, "You got appointment request", storeId,reqType);
                session.save(req);
                RequestDTO request = member.getRequest(req);
                session.update(member);
                transaction.commit();
                return request;
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(senderName));
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public boolean isApointee(String giverUserName, String userName, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, giverUserName);
            if (member != null) {
                UserRole role = member.getRoleOfStore(storeId);
                return role.isApointedByUser(userName);
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(giverUserName));
            }
        }
    }

    @Override
    public void clearGuestCart(int guestID) {
       try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Guest guest = session.get(Guest.class, guestID);
            if (guest != null) {
                guest.cart.clear();
                session.update(guest);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberGuestDoesntExistError(guestID));
            }
        }
    }

    @Override
    public void addProductToCart(int guestId, int storeId, int productId, int amount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Guest guest = session.get(Guest.class, guestId);
            if (guest != null) {
                guest.addProductToCart(storeId, productId, amount);
                session.update(guest);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberGuestDoesntExistError(guestId));
            }
        }
    }
    @Override
    public StoreManager createStoreManagerRole(int storeId) {
       try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            StoreManager role = new StoreManager(storeId);
            session.save(role);
            transaction.commit();
            return role;
        }
    }
    @Override
    public StoreOwner createStoreOwnerRole(int storeId, String apointee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            StoreOwner role = new StoreOwner(storeId, apointee);
            session.save(role);
            transaction.commit();
            return role;
        }
    } 
    public StoreFounder createStoreFounderRole(int storeId, String apointee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            StoreFounder role = new StoreFounder(storeId, apointee);
            session.save(role);
            transaction.commit();
            return role;
        }
    }  
    @Override
    public void clear() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE basket_products").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE baskets").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE role_permissions").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE user_orders").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE user_roles").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE Notification").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE role_appointments").executeUpdate();
                //session.createNativeQuery("TRUNCATE TABLE user_notifications").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE carts").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE guests").executeUpdate();
                session.createNativeQuery("TRUNCATE TABLE Members").executeUpdate();
                session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
                
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
    }
}
    
}
