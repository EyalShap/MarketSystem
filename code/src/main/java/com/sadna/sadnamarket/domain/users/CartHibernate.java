package com.sadna.sadnamarket.domain.users;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cart")
public class CartHibernate {

    @EmbeddedId
    private CartId cartId;

    private int quantity;

    public CartHibernate(int cartId,int storeId, int produceId, int quantity) {
        this.cartId.setCartId(cartId);; // Initialize the embedded id object
        this.cartId.setStoreId(storeId);
        this.cartId.setProduceId(produceId);
        this.quantity = quantity;
    }

    // Default constructor for Hibernate
    public CartHibernate() {
        this.cartId = new CartId();
    }

    public CartId getCartId() {
        return cartId;
    }

    public void setCartId(CartId cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Equals and hashCode methods
    // You can override equals and hashCode based on your business logic

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartHibernate that = (CartHibernate) o;
        return cartId.equals(that.cartId);
    }

    @Override
    public int hashCode() {
        return cartId.hashCode();
    }
}

