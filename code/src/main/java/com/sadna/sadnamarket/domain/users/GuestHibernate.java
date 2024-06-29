package com.sadna.sadnamarket.domain.users;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.sadna.sadnamarket.HibernateUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;


@Entity
@Table(name = "guests")
public class GuestHibernate {
    @Id
    @GeneratedValue
    private int guestId;
    @Column(name = "cart_id")
    private int cartId;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
        name = "cart",
        joinColumns = @JoinColumn(name = "cart_id",referencedColumnName = "cart_id")
    )
    private List<CartHibernate> cart;

    public int getGuestId() {
        return guestId;
    }
    public int getCartId() {
        return cartId;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
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
}
