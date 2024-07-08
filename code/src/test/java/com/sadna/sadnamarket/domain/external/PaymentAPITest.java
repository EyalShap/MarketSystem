package com.sadna.sadnamarket.domain.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.Config;
import com.sadna.sadnamarket.domain.orders.MemoryOrderRepository;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Date;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentAPITest {

    PaymentService service;

    @BeforeAll
    void setUp(){
        Config.PAYMENT_ENABLE = true;
        Config.PAYMENT_URL = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        service = PaymentService.getInstance();
    }

    @Test
    void paymentSuccessTest() throws JsonProcessingException {
        CreditCardDTO goodCard = new CreditCardDTO("2222333344445555", "262", new Date(1617311571), "204444444", "israel");
        Assert.assertTrue(service.pay(100,goodCard,null));
    }

    @Test
    void paymentFailureBadCardTest() throws JsonProcessingException {
        CreditCardDTO badCard = new CreditCardDTO("2222333344445555", "988", new Date(1617311571), "204444444", "israel");
        Assert.assertFalse(service.pay(100,badCard,null));
    }
}
