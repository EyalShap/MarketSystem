package com.sadna.sadnamarket;

import com.sadna.sadnamarket.domain.stores.IStoreRepository;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import com.sadna.sadnamarket.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.yaml.snakeyaml.error.Mark;

import java.time.LocalDate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SadnaMarketApplication {
	public static void main(String[] args) {
		SpringApplication.run(SadnaMarketApplication.class, args);
		
		//System.out.println(service.createStore("Alice", "hi").getDataJson());
		//System.out.println(service.createStore("Alice", "hi").getDataJson());

	}

}
