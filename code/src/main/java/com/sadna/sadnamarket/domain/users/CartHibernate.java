package com.sadna.sadnamarket.domain.users;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;


@Entity
@Table(name = "cart")
@Embeddable
public class CartHibernate {
    @Column
    private int cartId;
    @Column
    private int storeId;
    @Column
    private int produceId;
    @Column
    private int quantity;

    public CartHibernate(int storeId, int produceId, int quantity) {
        this.storeId = storeId;
        this.produceId = produceId;
        this.quantity = quantity;
    }
    public int getCartId() {
        return cartId;
    }
    public int getStoreId() {
        return storeId;
    }
    public int getProduceId() {
        return produceId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public void setProduceId(int produceId) {
        this.produceId = produceId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartHibernate that = (CartHibernate) o;
        return Objects.equals(cartId, that.cartId) && Objects.equals(storeId, that.storeId)&&Objects.equals(produceId, that.produceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, storeId, produceId);
    }
}
