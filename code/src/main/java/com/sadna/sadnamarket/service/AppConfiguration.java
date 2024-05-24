package com.sadna.sadnamarket.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public MarketService marketService(){
        return MarketService.getInstance();
    }

    @Bean
    public MarketServiceTestAdapter marketServiceAdapter(){
        return new MarketServiceTestAdapter();
    }
}
