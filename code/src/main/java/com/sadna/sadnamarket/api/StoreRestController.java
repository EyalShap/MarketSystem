package com.sadna.sadnamarket.api;

import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/stores")
public class StoreRestController {
    
    MarketService marketService = MarketService.getInstance();

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/createStore" -Method POST -Body "founderId=0&storeName=MyStore"
    @PostMapping("/createStore")
    public Response createStore(@RequestParam int founderId, @RequestParam String storeName) {
        return marketService.createStore(founderId, storeName);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/addProductToStore" -Method POST -Body "userId=1&storeId=0&productName=Apple&productQuantity=10&productPrice=5"
    @PostMapping("/addProductToStore")
    public Response addProductToStore(@RequestParam int userId, @RequestParam int storeId, @RequestParam String productName, @RequestParam int productQuantity, @RequestParam int productPrice) {
        return marketService.addProductToStore(userId, storeId, productName, productQuantity, productPrice);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/deleteProductFromStore?userId=1&storeId=0&productId=0" -Method DELETE
    @DeleteMapping("/deleteProductFromStore")
    public Response deleteProductFromStore(@RequestParam int userId, @RequestParam int storeId, @RequestParam int productId) {
        return marketService.deleteProductFromStore(userId, storeId, productId);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/updateProductInStore" -Method PUT -Body "userId=1&storeId=0&productId=0&newProductName=Banana&newQuantity=12&newPrice=4" -ContentType "application/x-www-form-urlencoded"
    @PutMapping("/updateProductInStore")
    public Response updateProductInStore(@RequestParam int userId, @RequestParam int storeId, @RequestParam int productId, @RequestParam String newProductName, @RequestParam int newQuantity, @RequestParam int newPrice) {
        return marketService.updateProductInStore(userId, storeId, productId, newProductName, newQuantity, newPrice);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/sendStoreOwnerRequest" -Method POST -Body "currentOwnerId=0&newOwnerId=1&storeId=0"
    @PostMapping("/sendStoreOwnerRequest")
    public Response sendStoreOwnerRequest(@RequestParam int currentOwnerId, @RequestParam int newOwnerId, @RequestParam int storeId) {
        return marketService.sendStoreOwnerRequest(currentOwnerId, newOwnerId, storeId);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/sendStoreManagerRequest" -Method POST -Body "currentOwnerId=0&newOwnerId=1&managerPermissions=0&managerPermissions=1&managerPermissions=2"
    @PostMapping("/sendStoreManagerRequest")
    public Response sendStoreManagerRequest(@RequestParam int currentOwnerId, @RequestParam int newManagerId, @RequestParam int storeId, @RequestParam Set<Integer> managerPermissions) {
        return marketService.sendStoreManagerRequest(currentOwnerId, newManagerId, storeId, managerPermissions);
    }

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

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/closeStore" -Method PUT -Body "userId=0&storeId=0" -ContentType "application/x-www-form-urlencoded"
    @PutMapping("/closeStore")
    public Response closeStore(@RequestParam int userId, @RequestParam int storeId) {
        return marketService.closeStore(userId, storeId);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getOwners" -Method GET -Body "userId=0&storeId=0"
    @GetMapping("/getOwners")
    public Response getOwners(@RequestParam int userId, @RequestParam int storeId) {
        return marketService.getOwners(userId, storeId);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getManagers" -Method GET -Body "userId=0&storeId=0"
    @GetMapping("/getManagers")
    public Response getManagers(@RequestParam int userId, @RequestParam int storeId) {
        return marketService.getManagers(userId, storeId);
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/stores/getSellers" -Method GET -Body "userId=0&storeId=0"
    @GetMapping("/getSellers")
    public Response getSellers(@RequestParam int userId, @RequestParam int storeId) {
        return marketService.getSellers(userId, storeId);
    }
}
