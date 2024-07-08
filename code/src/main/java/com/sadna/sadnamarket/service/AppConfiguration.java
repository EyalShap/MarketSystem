package com.sadna.sadnamarket.service;

import com.sadna.sadnamarket.Config;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

@Configuration
public class AppConfiguration {

    @Lazy(true)
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Profile("default")
    public MarketService marketService(ObjectProvider<RealtimeService> innerBeanProvider){
        return new MarketService(innerBeanProvider.getObject());
    }


    @Lazy(true)
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Profile("test")
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
