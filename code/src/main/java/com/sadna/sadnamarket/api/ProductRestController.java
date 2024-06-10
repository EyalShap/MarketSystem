package com.sadna.sadnamarket.api;

import com.sadna.sadnamarket.service.MarketService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {
    MarketService marketService = MarketService.getInstance();

    @GetMapping("/getAllProducts")
    public Response getAllProducts(@RequestParam String username, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.getAllProducts(token, username);
    }

    @GetMapping("/getFilteredProducts")
    public Response getFilteredProducts(@RequestParam String username,@RequestBody ProductRequest productRequest ,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.getFilteredProducts(token, username,productRequest.getProductName(),productRequest.getMinProductPrice(),productRequest.getMaxProductPrice(),productRequest.getProductCategory(),productRequest.getMinProductRank());
    }

}
