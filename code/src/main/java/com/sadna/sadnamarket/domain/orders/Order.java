package com.sadna.sadnamarket.domain.orders;


import java.util.Map;

public class Order {
    private int orderId;
    private String storeNameWhenOrdered;
    private Map<Integer, Integer> productAmounts;
    private Map<Integer, String> orderProductsJsons;

    public Order(int orderId,String storeNameWhenOrdered,Map<Integer, Integer> productAmounts,Map<Integer, String> orderProductsJsons){
        this.orderId=orderId;
        this.storeNameWhenOrdered=storeNameWhenOrdered;
        this.productAmounts=productAmounts;
        this.orderProductsJsons=orderProductsJsons;
    }

    public int getOrderId() {
        return orderId;
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
