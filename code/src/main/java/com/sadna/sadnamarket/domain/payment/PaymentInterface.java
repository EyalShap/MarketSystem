package com.sadna.sadnamarket.domain.payment;

import java.util.Date;

public interface PaymentInterface {
    public boolean creditCardValid(CreditCardDTO creditDetails);
    public boolean pay(double amount, CreditCardDTO payerCard, BankAccountDTO receiverAccount);
}
