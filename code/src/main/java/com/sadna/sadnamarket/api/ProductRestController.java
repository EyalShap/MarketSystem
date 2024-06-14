package com.sadna.sadnamarket.api;

import com.sadna.sadnamarket.service.MarketService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {
    @Autowired
    MarketService marketService;

    @GetMapping("/getAllProducts")
    public Response getAllProducts(@RequestParam String username) {
        return marketService.getAllProducts(username);
    }

    @GetMapping("/getFilteredProducts")
    public Response getFilteredProducts(@RequestParam String username,@RequestBody ProductRequest productRequest ) {

        return marketService.getFilteredProducts(username,productRequest.getProductName(),productRequest.getMinProductPrice(),productRequest.getMaxProductPrice(),productRequest.getProductCategory(),productRequest.getMinProductRank());
    }

}
