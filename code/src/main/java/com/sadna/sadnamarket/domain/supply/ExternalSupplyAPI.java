package com.sadna.sadnamarket.domain.supply;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.*;

public class ExternalSupplyAPI {
    private WebClient client;
    private ObjectMapper mapper;

    public ExternalSupplyAPI(){
        client = WebClient.create("https://damp-lynna-wsep-1984852e.koyeb.app/");
        mapper = new ObjectMapper();
    }
    public String handshake() throws JsonProcessingException {
        WSEPHandshakeRequest request = new WSEPHandshakeRequest();
        return sendRequest(request);
    }

    public String supply(String name, String address, String city, String country, String zip) throws JsonProcessingException {
        WSEPSupplyRequest request = new WSEPSupplyRequest();
        request.setName(name);
        request.setAddress(address);
        request.setCity(city);
        request.setCountry(country);
        request.setZip(zip);
        return sendRequest(request);
    }

    public String cancelSupply(String transaction) throws JsonProcessingException {
        WSEPCancelSupplyRequest request = new WSEPCancelSupplyRequest();
        request.setTransaction_id(transaction);
        return sendRequest(request);
    }

    private String sendRequest(WSEPRequest request) {
        return client.post().uri("").body(request.getBody()).retrieve().bodyToMono(String.class).block();
    }
}
