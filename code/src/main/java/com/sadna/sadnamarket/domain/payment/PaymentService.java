package com.sadna.sadnamarket.domain.payment;

public class PaymentService {
    private static PaymentService instance = null;
    private PaymentInterface controller;
    public static PaymentService getInstance(){
        if(instance == null){
            instance = new PaymentService();
        }
        return instance;
    }

    public PaymentService(){
        this.controller = new PaymentProxy();
    }

    public boolean checkCardValid(CreditCardDTO card){
        return controller.creditCardValid(card);
    }

    public boolean pay(double amount, CreditCardDTO card, BankAccountDTO bank){
        return controller.pay(amount, card, bank);
    }

    public void setController(PaymentInterface impl){
        this.controller = impl;
    }
}
