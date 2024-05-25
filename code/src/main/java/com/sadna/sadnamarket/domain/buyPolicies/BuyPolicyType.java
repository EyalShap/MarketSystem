package com.sadna.sadnamarket.domain.buyPolicies;

public enum BuyPolicyType {
    Default("Default");

    private final String value;

    BuyPolicyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BuyPolicyType fromValue(String value) {
        for (BuyPolicyType buyPolicyType : BuyPolicyType.values()) {
            if (buyPolicyType.value == value) {
                return buyPolicyType;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
