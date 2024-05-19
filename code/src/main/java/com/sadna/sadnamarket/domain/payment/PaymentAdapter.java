package com.sadna.sadnamarket.domain.payment;

public class PaymentAdapter implements PaymentInterface{
    @Override
    public boolean creditCardValid(CreditCardDTO creditDetails) {
        return false;
    }

    @Override
    public boolean pay(double amount, CreditCardDTO payerCard, BankAccountDTO receiverAccount) {
        return false;
    }

    public boolean implemented(){
        return false;
    }
}
