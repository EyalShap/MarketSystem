package com.sadna.sadnamarket.domain.users;

public enum Permission {
    ADD_PRODUCTS(1),
    DELETE_PRODUCTS(2),
    UPDATE_PRODUCTS(3),
    CLOSE_STORE(4),
    REOPEN_STORE(5),
    ADD_OWNER(6),
    ADD_MANAGER(7),
    ADD_SELLER(8),
    ADD_BUY_POLICY(9),
    ADD_DISCOUNT_POLICY(10);


    private final int value;

    Permission(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
