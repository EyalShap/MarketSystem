package com.sadna.sadnamarket.api;

public class WSEPHandshakeRequest extends WSEPRequest{
    @Override
    public String getAction_type() {
        return "handshake";
    }
}
