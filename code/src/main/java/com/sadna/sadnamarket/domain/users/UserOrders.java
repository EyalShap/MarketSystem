package com.sadna.sadnamarket.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_orders")
public class UserOrders {

    @Id
    private String username;
    private int orderId;


    public UserOrders(String username, int orderId) {
        this.username = username;
        this.orderId = orderId;
    }
    public String getUsername() {
        return username;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
