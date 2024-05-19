package com.sadna.sadnamarket.domain.payment;

public class PaymentProxy implements PaymentInterface{
    PaymentAdapter real;

    public PaymentProxy(){
        real = new PaymentAdapter();
    }

    @Override
    public boolean creditCardValid(CreditCardDTO creditDetails) {
        if(real.implemented()){
            return real.creditCardValid(creditDetails);
        }
        return false;
    }

    @Override
    public boolean pay(double amount, CreditCardDTO payerCard, BankAccountDTO receiverAccount) {
        if(real.implemented()){
            return real.pay(amount, payerCard, receiverAccount);
        }
        return false;
    }
}
