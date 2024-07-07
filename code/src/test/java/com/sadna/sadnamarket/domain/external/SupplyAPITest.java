package com.sadna.sadnamarket.domain.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.Config;
import com.sadna.sadnamarket.domain.orders.MemoryOrderRepository;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.supply.SupplyService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class SupplyAPITest {

    SupplyService service;

    @BeforeEach
    void setUp(){
        Config.SUPPLY_ENABLE = true;
        Config.SUPPLY_URL = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        service = SupplyService.getInstance();
    }

    @Test
    void supplySuccessTest() throws JsonProcessingException {
        AddressDTO addressDTO = new AddressDTO("Israel", "Rishon", "Street", "Street 2",
                "29382938", "Felix", "0520520520", "felix@gmail.com");
        String transaction = service.makeOrder(null,addressDTO);
        Assert.assertNotEquals("-1",transaction);
    }

    @Test
    void cancelSupplySuccessTest() throws JsonProcessingException {
        AddressDTO addressDTO = new AddressDTO("Israel", "Rishon", "Street", "Street 2",
                "29382938", "Felix", "0520520520", "felix@gmail.com");
        String transaction = service.makeOrder(null,addressDTO);
        Assert.assertTrue(service.cancelOrder(transaction));
    }

    @Test
    void cancelSupplyFailureTest() throws JsonProcessingException {
        AddressDTO addressDTO = new AddressDTO("Israel", "Rishon", "Street", "Street 2",
                "29382938", "Felix", "0520520520", "felix@gmail.com");
        String transaction = service.makeOrder(null,addressDTO);
        Assert.assertTrue(service.cancelOrder("transactionthatneverhappened"));
    }
}
