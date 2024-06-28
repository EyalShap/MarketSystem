package com.sadna.sadnamarket.domain.users;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import jakarta.persistence.GeneratedValue;

@Embeddable
public class CartId implements Serializable {

    @Column(name = "cart_id")
    private int cartId;

    @Column(name = "store_id")
    private int storeId;

    @Column(name = "product_id")
    private int produceId;

    // Constructors, getters, setters, equals, and hashCode methods
    // Ensure you implement equals and hashCode correctly for composite keys

    public CartId() {
    }

    public CartId(int cartId, int storeId, int produceId) {
        this.cartId = cartId;
        this.storeId = storeId;
        this.produceId = produceId;
    }

    // Getters and setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProduceId() {
        return produceId;
    }

    public void setProduceId(int produceId) {
        this.produceId = produceId;
    }

    // Equals and hashCode methods
    // Ensure you implement equals and hashCode correctly for composite keys

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartId cartId1 = (CartId) o;
        return cartId == cartId1.cartId &&
                storeId == cartId1.storeId &&
                produceId == cartId1.produceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, storeId, produceId);
    }
}
