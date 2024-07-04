package com.sadna.sadnamarket.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.payment.WSEPHandshakeRequest;
import org.springframework.web.reactive.function.client.WebClient;

public class ExternalPaymentAPI {
    private WebClient client;
    private ObjectMapper mapper;

    public ExternalPaymentAPI(){
        client = WebClient.create("https://damp-lynna-wsep-1984852e.koyeb.app/");
        mapper = new ObjectMapper();
    }

    public String handshake() throws JsonProcessingException {
        WSEPHandshakeRequest request = new WSEPHandshakeRequest();
        return sendRequest(request);
    }

        public String pay(double amount, String creditCardNumber, int expirationMonth, int expirationYear, String holderName, String cvv, String holderId) throws JsonProcessingException {
        WSEPPayRequest request = new WSEPPayRequest(amount, creditCardNumber, expirationMonth, expirationYear, holderName, cvv, holderId);
        return sendRequest(request);
    }

    private String sendRequest(WSEPRequest request) {
        return client.post().uri("").body(request.getBody()).retrieve().bodyToMono(String.class).block();
    }
}
