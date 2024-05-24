package com.sadna.sadnamarket;

import com.sadna.sadnamarket.domain.stores.IStoreRepository;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import com.sadna.sadnamarket.service.MarketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})


public class SadnaMarketApplication {
	public static void main(String[] args) {
		IStoreRepository repo = new MemoryStoreRepository();
		SpringApplication.run(SadnaMarketApplication.class, args);
		MarketService service = new MarketService(repo);
		//System.out.println(service.createStore("Alice", "hi").getDataJson());
		//System.out.println(service.createStore("Alice", "hi").getDataJson());

	}

}
