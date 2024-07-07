package com.sadna.sadnamarket.service;

import com.sadna.sadnamarket.Config;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfiguration {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MarketService marketService(ObjectProvider<RealtimeService> innerBeanProvider){
        return new MarketService(innerBeanProvider.getObject());
    }


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MarketService testingService(ObjectProvider<RealtimeService> innerBeanProvider){
        Config.read("testconfig.json");
        return new MarketService(innerBeanProvider.getObject());
    }

    @Bean
    public RealtimeService realtimeService(){
        return new RealtimeService();
    }

    @Bean
    public MarketServiceTestAdapter marketServiceAdapter(){
        return new MarketServiceTestAdapter();
    }
}
