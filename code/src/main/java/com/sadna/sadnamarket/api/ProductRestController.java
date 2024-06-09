package com.sadna.sadnamarket.api;

import com.sadna.sadnamarket.service.MarketService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {
    MarketService marketService = MarketService.getInstance();

    @PostMapping("/getAllProducts")
    public Response getAllProducts(@RequestParam String token, @RequestParam String username) {
        return marketService.getAllProducts(token, username);
    }

    @PostMapping("/getFilteredProducts")
    public Response getFilteredProducts(@RequestParam String token,@RequestParam String username,@RequestParam String productName,@RequestParam double minProductPrice,@RequestParam double maxProductPrice,@RequestParam String productCategory,@RequestParam double minProductRank) {
        return marketService.getFilteredProducts(token, username,productName,minProductPrice,maxProductPrice,productCategory,minProductRank);
    }

}
