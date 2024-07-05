package com.sadna.sadnamarket;

import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentInterface;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.stores.IStoreRepository;
import com.sadna.sadnamarket.domain.stores.MemoryStoreRepository;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.supply.OrderDetailsDTO;
import com.sadna.sadnamarket.domain.supply.SupplyInterface;
import com.sadna.sadnamarket.domain.supply.SupplyService;
import com.sadna.sadnamarket.service.MarketService;
import com.sadna.sadnamarket.service.RealtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.yaml.snakeyaml.error.Mark;

import java.time.LocalDate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SadnaMarketApplication {
	public static void main(String[] args) {
		/*PaymentService.getInstance().setController(new PaymentInterface() {
			@Override
			public boolean creditCardValid(CreditCardDTO creditDetails) {
				return true;
			}

			@Override
			public boolean pay(double amount, CreditCardDTO payerCard, BankAccountDTO receiverAccount) {
				return true;
			}
		});*/
		SpringApplication.run(SadnaMarketApplication.class, args);
		MarketService s = new MarketService(new RealtimeService());
		s.register("dana", "123", "dana", "friedman", "dana@gmail.com", "1234567890", LocalDate.of(2003, 5, 26));
		Response r = s.login("dana", "123");
		Response dfd = s.createStore(r.getDataJson(), "dana", "tehj", "jenewfew", "jdnwe@gmail.com", "0123456789");
		int id = Integer.parseInt(dfd.getDataJson());
		Response dnqw = s.addProductToStore(r.getDataJson(), "dana", id, "djw", 100, 4, "jewnfekl", 2, 3, "fwjeknfkejwfn");
		Response d = s.getStoreInfo(r.getDataJson(), "dana", id);
		String json = d.getDataJson();
		System.out.println(json);
		int xlw = 9;
		//System.out.println(service.createStore("Alice", "hi").getDataJson());
		//System.out.println(service.createStore("Alice", "hi").getDataJson());

	}

}
