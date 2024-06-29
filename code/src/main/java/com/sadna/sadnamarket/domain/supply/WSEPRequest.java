package com.sadna.sadnamarket.domain.supply;

public abstract class WSEPRequest {
    String action_type;

    public abstract String getAction_type();

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }
}
