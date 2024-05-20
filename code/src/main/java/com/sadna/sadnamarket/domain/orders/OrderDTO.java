package com.sadna.sadnamarket.domain.orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderDTO {
    // fields needed for order history
    private int orderId;
    private String storeNameWhenOrdered;
    private Map<Integer, Integer> productAmounts;
    private Map<Integer, String> orderProductsJsons;
    //other fields

    public String getStoreName() {
        // dana added this proxy function for the get store order history use case
        return "Proxy Store";
    }

    public List<Integer> getProductIds() {
        // dana added this proxy function for the get store order history use case
        return new ArrayList<>();
    }

    public int getProductAmount(int productId) {
        // dana added this proxy function for the get store order history use case
        return 0;
    }

    public String getProductDescription(int productId) {
        // dana added this proxy function for the get store order history use case
        return "";
    }

    public Date getOrderDate() {
        // dana added this proxy function for the get store order history use case
        return new Date();
    }
}
