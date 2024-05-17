package com.sadna.sadnamarket.api;

import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
public class StoreRestController {

    @Autowired
    private MarketService service;

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores" -Method POST -Body "founderId=1&storeName=MyStore"
    @PostMapping
    public Response createStore(@RequestParam int founderId, @RequestParam String storeName) {
        return service.createStore(founderId, storeName);
    }
}
