package com.sadna.sadnamarket.domain.users;

import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.Session;

import com.sadna.sadnamarket.HibernateUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Table(name = "Members")
public class MemberHibernate implements Serializable {
    @Id
    private String username;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String phoneNumber;
    @Column
    private boolean isLoggedIn;
    @Column
    private int notifyID;
    @Column
    private LocalDate birDate;

    @OneToMany
    @JoinTable(
        name = "User_roles",
        joinColumns = @JoinColumn(name = "username")
    )
    private List<UserRoleHibernate> roles;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_orders", joinColumns = @JoinColumn(name = "username"))
    @Column(name = "order_id")
    private List<Integer> orders;


    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cart_id")
    private int cartId;

    @OneToMany
    @JoinTable(
        name = "cart",
        joinColumns = @JoinColumn(name = "cart_id",referencedColumnName = "cart_id")
    )
    private List<CartHibernate> cart=new ArrayList<>();
    public MemberHibernate() {
    }
    public MemberHibernate(String username, String firstName, String lastName, String email, String phoneNumber,LocalDate birthDate,int notifyID, boolean isLoggedIn) { 
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isLoggedIn = false;
        this.notifyID = notifyID;
        this.isLoggedIn = isLoggedIn;
    }
    public MemberHibernate(String username, String firstName, String lastName, String email, String phoneNumber,LocalDate birthDate) { 
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isLoggedIn = false;
        this.notifyID = 0;
        this.isLoggedIn = false;
    }
    public String getUsername() {
        return username;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    public int getNotifyID() {
        return notifyID;
    }
    public void setNotifyID(int notifyID) {
        this.notifyID = notifyID;
    }
    public List<UserRoleHibernate> getRoles(){
        return roles;
    }
    public List<CartItemDTO> getCart() {
         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM CartHibernate WHERE cartId = :cartId";
            List<CartHibernate> cart = session.createQuery(hql, CartHibernate.class)
                                          .setParameter("cartId", cartId)
                                          .getResultList();
            List<CartItemDTO> cartItems = new ArrayList<>();
        for (CartHibernate cartHibernate : cart) {
            CartItemDTO cartItemDTO = new CartItemDTO(cartHibernate.getCartId().getStoreId(), cartHibernate.getCartId().getProduceId(), cartHibernate.getQuantity());
            cartItems.add(cartItemDTO);
        }
            return cartItems;
       }
    }
    public int getCartId() {
        return cartId;
    }
    public LocalDate getBirDate() {
        return birDate;
    }
    public void setBirDate(LocalDate birDate) {
        this.birDate = birDate;
    }

    public List<Integer> getOrders() {
        return orders;
    }
    public void addOrder(int orderId) {
        orders.add(orderId);
    }
}
