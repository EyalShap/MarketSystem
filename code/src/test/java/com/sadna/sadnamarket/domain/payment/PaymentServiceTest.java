package com.sadna.sadnamarket.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {
    private PaymentService service;
    private CreditCardDTO validCard;

    @BeforeEach
    void setUp() {
        this.service = new PaymentService();
        this.validCard = new CreditCardDTO("2222333344445555", "262", Date.valueOf(LocalDate.of(2021, 4, 13)), "20444444");
    }

    @Test
    void checkCardValid() {
        assertTrue(service.checkCardValid(validCard));
    }

    @Test
    void pay() throws JsonProcessingException {
        assertTrue(service.pay(1000, validCard, null));
    }
}