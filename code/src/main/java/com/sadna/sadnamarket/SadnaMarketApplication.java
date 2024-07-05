package com.sadna.sadnamarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SadnaMarketApplication {
	public static void main(String[] args) {
		SpringApplication.run(SadnaMarketApplication.class, args);
		SetupRunner setupRunner = new SetupRunner();
        setupRunner.setupFromJson("config.json");
		
	}

}
