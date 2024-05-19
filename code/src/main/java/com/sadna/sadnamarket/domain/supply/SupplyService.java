package com.sadna.sadnamarket.domain.supply;

public class SupplyService {
    private static SupplyService instance = null;
    private SupplyInterface controller;
    public static SupplyService getInstance(){
        if(instance == null){
            instance = new SupplyService();
        }
        return instance;
    }

    public SupplyService(){
        this.controller = new SupplyProxy();
    }

    public boolean canMakeOrder(OrderDetailsDTO orderDetails, AddressDTO address) {
        return controller.canMakeOrder(orderDetails, address);
    }

    public String makeOrder(OrderDetailsDTO orderDetails, AddressDTO address) {
        return controller.makeOrder(orderDetails, address);
    }

    public boolean cancelOrder(String orderCode) {
        return controller.cancelOrder(orderCode);
    }

    public void setController(SupplyInterface impl){
        this.controller = impl;
    }
}
