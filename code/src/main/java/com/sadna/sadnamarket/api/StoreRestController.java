package com.sadna.sadnamarket.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.service.MarketService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/stores")
public class StoreRestController {

    // don't need this for version 1


    MarketService marketService = MarketService.getInstance();
    private static ObjectMapper objectMapper = new ObjectMapper();

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/createStore" -Method POST -Body "founderId=0&storeName=MyStore"
    @PostMapping("/createStore")
    public Response createStore(@RequestBody CreateStoreRequest createStoreRequest, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createStore(token,createStoreRequest.getFounderUsername(),createStoreRequest.getStoreName(),createStoreRequest.getAddress(),createStoreRequest.getEmail(),createStoreRequest.getPhoneNumber(),createStoreRequest.getOpeningHours(),createStoreRequest.getClosingHours());
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/addProductToStore" -Method POST -Body "userId=1&storeId=0&productName=Apple&productQuantity=10&productPrice=5"
    @PostMapping("/addProductToStore")
    public Response addProductToStore(@RequestParam  String username,@RequestBody ProductStoreRequest productStoreRequest, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.addProductToStore(token,username,productStoreRequest.getStoreId(),productStoreRequest.getProductName(),productStoreRequest.getProductQuantity(),productStoreRequest.getProductPrice(),productStoreRequest.getCategory(),productStoreRequest.getRank(),productStoreRequest.getProductWeight());
    }

    @PatchMapping("/setStoreBankAccount")
    public Response setStoreBankAccount(@RequestParam  String username, @RequestParam int storeId, @RequestParam BankAccountDTO bankAccoun , HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.setStoreBankAccount(token,username,storeId,bankAccoun);
    }


    @DeleteMapping("/deleteProductFromStore")
    public Response deleteProductFromStore(@RequestParam  String username,@RequestBody ProductStoreRequest productStoreRequest , HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.deleteProductFromStore(token,username, productStoreRequest.getStoreId(), productStoreRequest.getProductId());
    }


    @PutMapping("/updateProductInStore")
    public Response updateProductInStore(@RequestParam String username, @RequestBody ProductStoreRequest productStoreRequest , HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.updateProductInStore(token, username, productStoreRequest.getStoreId(),productStoreRequest.getProductId(),productStoreRequest.getProductName(),productStoreRequest.getProductQuantity(),productStoreRequest.getProductPrice(),productStoreRequest.getCategory(),productStoreRequest.getRank());
    }


    @PutMapping("/updateProductAmountInStore")
    public Response updateProductAmountInStore(@RequestParam String username, @RequestBody ProductStoreRequest productStoreRequest , HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.updateProductAmountInStore(token, username, productStoreRequest.getStoreId(),productStoreRequest.getProductId(),productStoreRequest.getProductQuantity());
    }


    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/sendStoreOwnerRequest" -Method POST -Body "currentOwnerId=0&newOwnerId=1&storeId=0"
    @PostMapping("/sendStoreOwnerRequest")
    public Response sendStoreOwnerRequest(@RequestParam String currentOwnerUsername, @RequestBody StoreRequest storeRequest, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.sendStoreOwnerRequest(token,currentOwnerUsername, storeRequest.getOwner(), storeRequest.getStoreId());
    }


    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/sendStoreManagerRequest" -Method POST -Body "currentOwnerId=0&newOwnerId=1&managerPermissions=0&managerPermissions=1&managerPermissions=2"
    @PostMapping("/sendStoreManagerRequest")
    public Response sendStoreManagerRequest(@RequestParam String currentOwnerUsername,@RequestParam String newManagerUsername,@RequestBody int storeId,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.sendStoreManagerRequest(token,currentOwnerUsername,newManagerUsername,storeId);
    }

    /*

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/acceptStoreOwnerRequest" -Method POST -Body "currentOwnerId=0&storeId=0"
    @PostMapping("/acceptStoreOwnerRequest")
    public Response acceptStoreOwnerRequest(@RequestParam int newOwnerId, @RequestParam int storeId) {
        return marketService.acceptStoreOwnerRequest(newOwnerId, storeId);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/acceptStoreManagerRequest" -Method POST -Body "newManagerId=1&storeId=0"
    @PostMapping("/acceptStoreManagerRequest")
    public Response acceptStoreManagerRequest(@RequestParam int newManagerId, @RequestParam int storeId) {
        return marketService.acceptStoreManagerRequest(newManagerId, storeId);
    }
    */


    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/closeStore" -Method PUT -Body "userId=0&storeId=0" -ContentType "application/x-www-form-urlencoded"
    @PutMapping("/closeStore")
    public Response closeStore(@RequestParam String username, @RequestParam int storeId ,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.closeStore(token,username, storeId);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getStoreOrderHistory?userId=0&storeId=0" -Method GET
    @GetMapping("/getOwners")
    public Response getOwners(@RequestParam String username, @RequestParam int storeId,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.getOwners(token,username, storeId);
    }


    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getStoreOrderHistory?userId=0&storeId=0" -Method GET
    @GetMapping("/getManagers")
    public Response getManagers(@RequestParam String username, @RequestParam int storeId,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.getManagers(token,username, storeId);
    }
    /*

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getStoreOrderHistory?userId=0&storeId=0" -Method GET
    @GetMapping("/getSellers")
    public Response getSellers(@RequestParam int userId, @RequestParam int storeId) {
        return marketService.getSellers(userId, storeId);
    }
    */

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getStoreOrderHistory?userId=0&storeId=0" -Method GET
    @GetMapping("/getStoreOrderHistory")
    public Response getStoreOrderHistory(@RequestParam String username, @RequestParam int storeId ,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.getStoreOrderHistory(token,username, storeId);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getStoreInfo?storeId=0" -Method GET
    @GetMapping("/getStoreInfo")
    public Response getStoreInfo(@RequestParam String username,@RequestParam int storeId,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        Response response = marketService.getStoreInfo(token,username,storeId);
        if(response.getError()) {
            return response;
        }

        Response res;
        try {
            StoreDTO storeDTO = objectMapper.readValue(response.getDataJson(), StoreDTO.class);
            res = Response.createResponse(false, objectMapper.writeValueAsString(String.format("Store id: %d, Store name: %s", storeDTO.getStoreId(), storeDTO.getStoreName())));
        }
        catch(Exception e) {
            res = Response.createResponse(true, e.getMessage());
        }

        return res;
    }

    @GetMapping("/getProductInfo")
    public Response getProductInfo(@RequestParam String username, @RequestParam int  productId ,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.getProductInfo(token,username,productId);
    }

    @GetMapping("/getStoreProductsInfo")
    public Response getStoreProductsInfo(@RequestParam String username, @RequestBody ProductStoreRequest productStoreRequest ,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.getStoreProductsInfo(token,username,productStoreRequest.getStoreId(),productStoreRequest.getProductName(),productStoreRequest.getCategory(),productStoreRequest.getProductPrice(),productStoreRequest.getRank());
    }


    @GetMapping("/getStoreProductAmount")
    public Response getStoreProductAmount(@RequestParam String username, @RequestBody ProductStoreRequest productStoreRequest ,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.getStoreProductAmount(token,username,productStoreRequest.getStoreId(),productStoreRequest.getProductId());
    }

    @PatchMapping("/changeManagerPermission")
    public Response changeManagerPermission(@RequestParam String username, @RequestBody ManagerPermissionRequest managerPermissionRequest ,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.changeManagerPermission(token,username,managerPermissionRequest.getManagerUsername(),managerPermissionRequest.getStoreId(),managerPermissionRequest.getPermission());
    }


    @GetMapping("/getManagerPermissions")
    public Response getManagerPermissions(@RequestParam String currentOwnerUsername,@RequestParam String managerUsername,@RequestParam  int storeId,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.getManagerPermissions(token,currentOwnerUsername,managerUsername,storeId);
    }

    /*
    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getProductsInfo?storeId=0" -Method GET
    @GetMapping("/getProductsInfo")
    public Response getProductsInfo(@RequestParam int storeId) {
        Response response = marketService.getProductsInfo(storeId);
        if(response.getError()) {
            return response;
        }

        Response res;
        List<String> productInfos = new ArrayList<>();
        try {
            Map<String, Integer> products = objectMapper.readValue(response.getDataJson(), new TypeReference<Map<String, Integer>>() {});
            for(String productJson : products.keySet()) {
                ProductDTO product = objectMapper.readValue(productJson, ProductDTO.class);
                productInfos.add(String.format("Product id: %d, Product name: %s, Product amount: %d, Product price: %d", product.getProductId(), product.getProductName(), products.get(productJson), product.getProductPrice()));
            }
            res = Response.createResponse(false, objectMapper.writeValueAsString(productInfos));
        }
        catch(Exception e) {
            res = Response.createResponse(true, e.getMessage());
        }

        return res;
    }
    */

}
