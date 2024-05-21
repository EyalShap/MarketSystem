package com.sadna.sadnamarket.domain.orders;

import java.util.Map;

public class Order {
    private int userId;
    private String storeNameWhenOrdered;
    private Map<Integer, Integer> productAmounts;
    private Map<Integer, String> orderProductsJsons;

    public Order(int userId,String storeNameWhenOrdered,Map<Integer, Integer> productAmounts,Map<Integer, String> orderProductsJsons){
        this.userId=userId;
        this.storeNameWhenOrdered = storeNameWhenOrdered;
        this.productAmounts = productAmounts;
        this.orderProductsJsons = orderProductsJsons;
    }

    public int getUserId() {
        return userId;
    }

    public String getStoreNameWhenOrdered() {
        return storeNameWhenOrdered;
    }

    public Map<Integer, Integer> getProductAmounts() {
        return productAmounts;
    }

    public Map<Integer, String> getOrderProductsJsons() {
        return orderProductsJsons;
    }
}
