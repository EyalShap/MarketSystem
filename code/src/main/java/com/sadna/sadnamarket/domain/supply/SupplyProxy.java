package com.sadna.sadnamarket.domain.supply;

public class SupplyProxy implements SupplyInterface {
    SupplyAdapter real;

    public SupplyProxy(){
        real = new SupplyAdapter();
    }

    @Override
    public boolean canMakeOrder(OrderDetailsDTO orderDetails, AddressDTO address) {
        if(real.implemented()){
            return real.canMakeOrder(orderDetails, address);
        }
        return false;
    }

    @Override
    public String makeOrder(OrderDetailsDTO orderDetails, AddressDTO address) {
        if(real.implemented()){
            return real.makeOrder(orderDetails, address);
        }
        return null;
    }

    @Override
    public boolean cancelOrder(String orderCode) {
        if(real.implemented()){
            return real.cancelOrder(orderCode);
        }
        return false;
    }
}
