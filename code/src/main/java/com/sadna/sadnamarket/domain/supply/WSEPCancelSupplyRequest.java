package com.sadna.sadnamarket.api;

public class WSEPCancelSupplyRequest extends WSEPRequest{
    String transaction_id;

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }


    @Override
    public String getAction_type() {
        return "cancel_supply";
    }
}
