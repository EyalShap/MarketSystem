package com.sadna.sadnamarket.domain.supply;

public class WSEPHandshakeRequest extends WSEPRequest{
    @Override
    public String getAction_type() {
        return "handshake";
    }
}
