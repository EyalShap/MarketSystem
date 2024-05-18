package com.sadna.sadnamarket.api;

import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
public class StoreRestController {

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/createStore" -Method POST -Body "founderId=1&storeName=MyStore"
    @PostMapping("/createStore")
    public Response createStore(@RequestParam int founderId, @RequestParam String storeName) {
        return MarketService.getInstance().createStore(founderId, storeName);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/addProductToStore" -Method POST -Body "userId=1&storeId=0&productName=Apple"
    @PostMapping("/addProductToStore")
    public Response addProductToStore(@RequestParam int userId, @RequestParam int storeId, @RequestParam String productName) {
        return MarketService.getInstance().addProductToStore(userId, storeId, productName);
    }
}
