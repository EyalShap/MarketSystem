package com.sadna.sadnamarket.domain.payment;

import java.util.Date;

public class CreditCardDTO {
    String creditCardNumber;
    String digitsOnTheBack;
    Date expirationDate;
    String ownerId;

    public CreditCardDTO(String creditCardNumber, String digitsOnTheBack, Date expirationDate, String ownerId) {
        this.creditCardNumber = creditCardNumber;
        this.digitsOnTheBack = digitsOnTheBack;
        this.expirationDate = expirationDate;
        this.ownerId = ownerId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getDigitsOnTheBack() {
        return digitsOnTheBack;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
