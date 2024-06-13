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
    @CrossOrigin(origins = "*",allowedHeaders = "*") // Allow cross-origin requests from any source
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


    @PostMapping("/createProductKgBuyPolicy")
    public Response createProductKgBuyPolicy(@RequestParam String username,@RequestBody PolicyWeightRequest policyWeightRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createProductKgBuyPolicy(token,username,policyWeightRequest.getProductId(),policyWeightRequest.getBuyTypes(),policyWeightRequest.getMinWeight(),policyWeightRequest.getMaxWeight());
    }


    @PostMapping("/createProductAmountBuyPolicy")
    public Response createProductAmountBuyPolicy(@RequestParam String username,@RequestBody PolicyAmountRequest policyAmountRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createProductAmountBuyPolicy(token,username,policyAmountRequest.getProductId(),policyAmountRequest.getBuyTypes(),policyAmountRequest.getMinAmount(),policyAmountRequest.getMaxAmount());
    }


    @PostMapping("/createCategoryAgeLimitBuyPolicy")
    public Response createCategoryAgeLimitBuyPolicy(@RequestParam String username,@RequestBody PolicyAgeRequest policyAgeRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createCategoryAgeLimitBuyPolicy(token,username,policyAgeRequest.getCategory(),policyAgeRequest.getBuyTypes(),policyAgeRequest.getMinAge(),policyAgeRequest.getMaxAge());
    }

    @PostMapping("/createCategoryHourLimitBuyPolicy")
    public Response createCategoryHourLimitBuyPolicy(@RequestParam String username,@RequestBody PolicyHourRequest policyHourRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createCategoryHourLimitBuyPolicy(token,username,policyHourRequest.getCategory(),policyHourRequest.getBuyTypes(),policyHourRequest.getFromHour(),policyHourRequest.getToHour());
    }


    @PostMapping("/createCategoryRoshChodeshBuyPolicy")
    public Response createCategoryRoshChodeshBuyPolicy(@RequestParam String username,@RequestBody PolicyAgeRequest policyAgeRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createCategoryRoshChodeshBuyPolicy(token,username,policyAgeRequest.getCategory(),policyAgeRequest.getBuyTypes());
    }

    @PostMapping("/createCategoryHolidayBuyPolicy")
    public Response createCategoryHolidayBuyPolicy(@RequestParam String username,@RequestBody PolicyAgeRequest policyAgeRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createCategoryHolidayBuyPolicy(token,username,policyAgeRequest.getCategory(),policyAgeRequest.getBuyTypes());
    }

    @PostMapping("/createAndBuyPolicy")
    public Response createAndBuyPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createAndBuyPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }

    @PostMapping("/createOrBuyPolicy")
    public Response createOrBuyPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createOrBuyPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }


    @PostMapping("/createConditioningBuyPolicy")
    public Response createConditioningBuyPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createConditioningBuyPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }


    @PostMapping("/addBuyPolicyToStore")
    public Response addBuyPolicyToStore(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.addBuyPolicyToStore(token,username,policyIdRequest.getStoreId(),policyIdRequest.getPolicyId1());
    }

    @PatchMapping("/removeBuyPolicyFromStore")
    public Response removeBuyPolicyFromStore(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.removeBuyPolicyFromStore(token,username,policyIdRequest.getStoreId(),policyIdRequest.getPolicyId1());
    }


    @PostMapping("/createMinProductOnStoreCondition")
    public Response createMinProductOnStoreCondition(@RequestParam String username,@RequestParam int minAmount,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createMinProductOnStoreCondition(token,minAmount,username);
    }

    @PostMapping("/createMinProductOnCategoryCondition")
    public Response createMinProductOnCategoryCondition(@RequestParam String username,@RequestBody PolicyAmountRequest policyAmountRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createMinProductOnCategoryCondition(token,policyAmountRequest.getMinAmount(),policyAmountRequest.getCategoryName(),username);
    }

    @PostMapping("/createMinProductCondition")
    public Response createMinProductCondition(@RequestParam String username,@RequestBody PolicyAmountRequest policyAmountRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createMinProductCondition(token,policyAmountRequest.getMinAmount(),policyAmountRequest.getProductName(),username);
    }

    @PostMapping("/createTrueCondition")
    public Response createTrueCondition(@RequestParam String username,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createTrueCondition(token,username);
    }

    @PostMapping("/createMinBuyCondition")
    public Response createMinBuyCondition(@RequestParam String username,@RequestParam int minBuy,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createMinBuyCondition(token,username,minBuy);
    }

    @PostMapping("/createXorCondition")
    public Response createXorCondition(@RequestParam String username,@RequestBody PolicyConditionRequest policyConditionRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createXorCondition(token,username,policyConditionRequest.getConditionAID(),policyConditionRequest.getConditionBID());
    }

    @PostMapping("/createOrCondition")
    public Response createOrCondition(@RequestParam String username,@RequestBody PolicyConditionRequest policyConditionRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createOrCondition(token,username,policyConditionRequest.getConditionAID(),policyConditionRequest.getConditionBID());
    }

    @PostMapping("/createAndCondition")
    public Response createAndCondition(@RequestParam String username,@RequestBody PolicyConditionRequest policyConditionRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createAndCondition(token,username,policyConditionRequest.getConditionAID(),policyConditionRequest.getConditionBID());
    }

    @PostMapping("/createOnProductSimpleDiscountPolicy")
    public Response createOnProductSimpleDiscountPolicy(@RequestParam String username,@RequestBody PolicyConditionRequest policyConditionRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createOnProductSimpleDiscountPolicy(token,username,policyConditionRequest.getPercentage(),policyConditionRequest.getProductName(),policyConditionRequest.getConditionAID());
    }
    @PostMapping("/createOnCategorySimpleDiscountPolicy")
    public Response createOnCategorySimpleDiscountPolicy(@RequestParam String username,@RequestBody PolicyConditionRequest policyConditionRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createOnCategorySimpleDiscountPolicy(token,username,policyConditionRequest.getPercentage(),policyConditionRequest.getCategoryName(),policyConditionRequest.getConditionAID());
    }


    @PostMapping("/createOnStoreSimpleDiscountPolicy")
    public Response createOnStoreSimpleDiscountPolicy(@RequestParam String username,@RequestBody PolicyConditionRequest policyConditionRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createOnStoreSimpleDiscountPolicy(token,username,policyConditionRequest.getPercentage(),policyConditionRequest.getConditionAID());
    }

    @PostMapping("/createTakeMaxXorDiscountPolicy")
    public Response createTakeMaxXorDiscountPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createTakeMaxXorDiscountPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }

    @PostMapping("/createTakeMinXorDiscountPolicy")
    public Response createTakeMinXorDiscountPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createTakeMinXorDiscountPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }

    @PostMapping("/createAdditionDiscountPolicy")
    public Response createAdditionDiscountPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createAdditionDiscountPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }


    @PostMapping("/createMaximumDiscountPolicy")
    public Response createMaximumDiscountPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createMaximumDiscountPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }

    @PostMapping("/createAndDiscountPolicy")
    public Response createAndDiscountPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createAndDiscountPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }

    @PostMapping("/createOrDiscountPolicy")
    public Response createOrDiscountPolicy(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.createOrDiscountPolicy(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }


    @PostMapping("/addDiscountPolicyToStore")
    public Response addDiscountPolicyToStore(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.addDiscountPolicyToStore(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }

    @PatchMapping("/removeDiscountPolicyToStore")
    public Response removeDiscountPolicyToStore(@RequestParam String username,@RequestBody PolicyIdRequest policyIdRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.removeDiscountPolicyToStore(token,username,policyIdRequest.getPolicyId1(),policyIdRequest.getPolicyId2());
    }


}
