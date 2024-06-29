package com.sadna.sadnamarket.domain.users;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sadna.sadnamarket.service.Error;

import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.domain.auth.UserCredential;

public class UserHibernateRepo implements IUserRepository {


    @Override
    public void store(String username,String firstName, String lastName,String emailAddress,String phoneNumber, LocalDate birthDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MemberHibernate member = new MemberHibernate(username, firstName, lastName, emailAddress, phoneNumber,birthDate);
            session.save(member);
            transaction.commit();
        }
    }

    @Override
    public boolean hasMember(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            MemberHibernate member = session.get(MemberHibernate.class, name);
            if (member != null) {
                return true;
            } else {
                return false;
                
            }
        }
    }

    @Override
    public int addGuest() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            GuestHibernate guest = new GuestHibernate();
            session.save(guest);
            transaction.commit();
            return guest.getGuestId();
        } 
    }

    @Override
    public void deleteGuest(int guestID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            GuestHibernate guest = session.get(GuestHibernate.class, guestID);
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
    public List<CartItemDTO> getUserCart(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            MemberHibernate member = session.get(MemberHibernate.class, username);
            if (member != null) {
                return member.getCart();
            } else {
                return null;
            }
        }
    }

    @Override
    public List<CartItemDTO> getGuestCart(int guestID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            GuestHibernate guest = session.get(GuestHibernate.class, guestID);
            if (guest != null) {
                return guest.getCart();
            } else {
                throw new NoSuchElementException(Error.makeMemberGuestDoesntExistError(guestID));
            }
        }
    }

    @Override
    public boolean hasPermissionToRole(String userName, Permission permission, int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM MemberHibernate m WHERE m.username = :username";
            MemberHibernate member = session.createQuery(hql, MemberHibernate.class)
                                            .setParameter("username", userName)
                                            .uniqueResult();
    
            if (member != null) {
                for (UserRoleHibernate role : member.getRoles()) {
                    if (role.getStoreId() == storeId) {
                        return role.hasPermission(permission);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public NotificationDTO addNotification(String userName, String msg) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNotification'");
    }

    @Override
    public boolean isLoggedIn(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            MemberHibernate member = session.get(MemberHibernate.class, username);
            return member != null && member.isLoggedIn();
        }
    }

    @Override
    public void setLogin(String userName, boolean b) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MemberHibernate member = session.get(MemberHibernate.class, userName);
            if (member != null) {
                member.setLoggedIn(b);
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
            MemberHibernate member = session.get(MemberHibernate.class, username);
            if (member != null) {
                CartHibernate cartItem = new CartHibernate(member.getCartId(),storeId, productId, amount);;
                session.save(cartItem);
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
            MemberHibernate member = session.get(MemberHibernate.class, username);
            if (member != null) {
                int cartId=member.getCartId();
                String hql = "FROM cart m WHERE m.cartId = :cartId";
                List<CartHibernate> itemToRemove =session.createQuery(hql, CartHibernate.class).setParameter("CartId", cartId).list();
                boolean found = false;
                for (CartHibernate cartHibernate : itemToRemove) {
                    if(cartHibernate.getCartId().getStoreId()==storeId && cartHibernate.getCartId().getProduceId()==productId)
                        session.delete(itemToRemove);
                        session.update(member);
                        found = true;
                        transaction.commit();
                }
                if (!found) {
                    transaction.rollback();
                    throw new NoSuchElementException(Error.makeBasketProductDoesntExistError());
                }
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
            GuestHibernate guest = session.get(GuestHibernate.class, guestId);
            if (guest != null) {
                int cartId=guest.getCartId();
                String hql = "FROM cart m WHERE m.cartId = :cartId";
                List<CartHibernate> itemToRemove =session.createQuery(hql, CartHibernate.class).setParameter("CartId", cartId).list();
                boolean found = false;
                for (CartHibernate cartHibernate : itemToRemove) {
                    if(cartHibernate.getCartId().getStoreId()==storeId && cartHibernate.getCartId().getProduceId()==productId)
                        session.delete(itemToRemove);
                        session.update(guest);
                        found = true;
                        transaction.commit();
                }
                if (!found) {
                    transaction.rollback();
                    throw new NoSuchElementException(Error.makeBasketProductDoesntExistError());
                }
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
            MemberHibernate member = session.get(MemberHibernate.class, username);
            if (member != null) {
                int cartId=member.getCartId();                
                String hql = "FROM cart m WHERE m.username = :username AND m.storeId = :storeId AND m.productId = :productId";
                List<CartHibernate> cart = session.createQuery(hql, CartHibernate.class).setParameter("CartId", cartId).list();
                for (CartHibernate item : cart) {
                    if (item.getCartId().getStoreId() == storeId && item.getCartId().getProduceId() == productId) {
                        item.setQuantity(amount);
                        session.update(item);
                        transaction.commit();
                        return;
                    }
                }
                transaction.rollback();
                throw new NoSuchElementException("Product not found in cart");
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
            GuestHibernate guest = session.get(GuestHibernate.class, guestId);
            if (guest != null) {
                int cartId=guest.getCartId();                
                String hql = "FROM cart m WHERE m.username = :username AND m.storeId = :storeId AND m.productId = :productId";
                List<CartHibernate> cart = session.createQuery(hql, CartHibernate.class).setParameter("CartId", cartId).list();
                for (CartHibernate item : cart) {
                    if (item.getCartId().getStoreId() == storeId && item.getCartId().getProduceId() == productId) {
                        item.setQuantity(amount);
                        session.update(item);
                        transaction.commit();
                        return;
                    }
                }
                transaction.rollback();
                throw new NoSuchElementException("Product not found in cart");
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
            MemberHibernate member = session.get(MemberHibernate.class, userName);
            if (member != null) {
                member.setLoggedIn(false);;
                session.update(member);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public void setCart(String userName, List<CartItemDTO> cart) {
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MemberHibernate member = session.get(MemberHibernate.class, userName);
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
            MemberHibernate member = session.get(MemberHibernate.class, userName);
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
            MemberHibernate member = session.get(MemberHibernate.class, userName);
            if (member != null) {
                member.setEmail(emailAddress);
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
            MemberHibernate member = session.get(MemberHibernate.class, userName);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBirthday'");
    }

    @Override
    public MemberDTO getMemberDTO(String userName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            MemberHibernate member = session.get(MemberHibernate.class, userName);
            if (member != null) {
                return new MemberDTO(member.getUsername(), member.getFirstName(), member.getLastName(), member.getEmail(), member.getPhoneNumber(),member.getBirDate().toString());
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(userName));
            }
        }
    }

    @Override
    public List<Integer> getOrdersHistory(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            MemberHibernate member = session.get(MemberHibernate.class, username);
            if (member != null) {
                return member.getOrders();
            } else {
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public void clearCart(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MemberHibernate member = session.get(MemberHibernate.class, username);
            if (member != null) {
                int cartId = member.getCartId();
                String hql = "DELETE FROM CartHibernate WHERE cartId = :cartId";
                session.createQuery(hql).setParameter("cartId", cartId).executeUpdate();
                transaction.commit();
            } else {
                transaction.rollback();
                throw new NoSuchElementException(Error.makeMemberUserDoesntExistError(username));
            }
        }
    }

    @Override
    public List<NotificationDTO> getNotifications(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNotifications'");
    }

    @Override
    public void addOrder(String username, int orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MemberHibernate member = session.get(MemberHibernate.class, username);
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

    @Override
    public void clearGuestCart(int guestID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearGuestCart'");
    }

    @Override
    public void addProductToCart(int guestId, int storeId, int productId, int amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addProductToCart'");
    }
    @Override
    public void clear() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = "DELETE FROM MemberHibernate";
            session.createQuery(hql).executeUpdate();
            transaction.commit();
        }
    }
    
}
