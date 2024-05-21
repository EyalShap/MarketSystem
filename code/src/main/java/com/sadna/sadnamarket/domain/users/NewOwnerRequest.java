package com.sadna.sadnamarket.domain.users;

public class NewOwnerRequest extends Notification{
    private int storeId;
    private boolean accepted;

    public NewOwnerRequest(int storeId) {
        this.storeId = storeId;
        this.accepted = false;
    }

    public void accept() {
        this.accepted = true;
    }
}
