package com.sadna.sadnamarket.domain.users;

public enum Permission {
    ADD_PRODUCTS(1),
    DELETE_PRODUCTS(2),
    UPDATE_PRODUCTS(3),
    CLOSE_STORE(4),
    REOPEN_STORE(5);

    private final int value;

    Permission(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
