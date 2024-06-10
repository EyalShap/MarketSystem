package com.sadna.sadnamarket.domain.orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderDTO {
    private String memberName;
    //private int orderId;
    private String storeNameWhenOrdered;
    private Map<Integer, Integer> productAmounts;
    private Map<Integer, String> orderProductsJsons;

    public OrderDTO(String memberName,String storeNameWhenOrdered,Map<Integer, Integer> productAmounts,Map<Integer, String> orderProductsJsons){
        this.memberName=memberName;
        this.storeNameWhenOrdered=storeNameWhenOrdered;
        this.productAmounts=productAmounts;
        this.orderProductsJsons=orderProductsJsons;
    }

    public String getMemberName() {
        return memberName;
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
