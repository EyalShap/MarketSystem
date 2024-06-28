package com.sadna.sadnamarket.domain.users;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;


@Entity
@Table(name = "guests")
public class GuestHibernate {
    @GeneratedValue
    private int guestId;
    @Column
    private int cartId;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
        name = "cart",
        joinColumns = @JoinColumn(name = "cartId")
    )
    private CartHibernate cart;

    public int getGuestId() {
        return guestId;
    }
    public int getCartId() {
        return cartId;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


}
