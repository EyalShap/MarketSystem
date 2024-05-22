package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.users.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class MarketServiceTestAdapter {
    //this is an adapter for the tests
    //please add the true configuration here when you make additions to the actual service
    //thanks
    @Autowired
    MarketService real;

    ObjectMapper objectMapper = new ObjectMapper();


    //attention
    //String userId is actually username
    //i am lazy


    public boolean reset(){
        //function that resets the service and repositories
        return true;
        //please implement when possible
    }
    public Response guestEnterSystem(){
        return Response.createResponse(false, "Imagine this is session ID or something");
    }

    public Response guestLeaveSystem(String uuid){
        return Response.createResponse(false, "true");
    }

    public Response guestCartExists(String uuid){
        return Response.createResponse(false, "false");
    }

    public Response signUp(String uuid, String email, String username, String passwordHash){
        return Response.createResponse(false, "3424234"); //returns token
    }

    public Response memberExists(String userId){
        return Response.createResponse(false, "true");
    }

    public Response authenticate(String token, String username){
        return Response.createResponse(false, "true");
    }

    public Response login(String username, String passwordHash){
        return Response.createResponse(false, "3424234"); //returns token
    }

    public Response logout(String token){
        return Response.createResponse(false, "true");
    }

    public Response getStoreData(String token, String userId, int storeId) throws JsonProcessingException {
        //empty token and -1 userId if guest
        StoreDTO dto = new StoreDTO(4,true,"TestStore", new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        return Response.createResponse(false, objectMapper.writeValueAsString(dto));
    }

    public Response getProductData(String token, String userId, int productId) throws JsonProcessingException {
        //empty token and -1 userId if guest
        ProductDTO dto = new ProductDTO(5, "TestProduct", 0, "product", true);
        return Response.createResponse(false, objectMapper.writeValueAsString(dto));
    }

    public Response searchProduct(String productName, double productPriceMin, double productPriceMax, String productCategory, int storeRating, int productRating) throws JsonProcessingException {
        ProductDTO dto = new ProductDTO(0, productName, productPriceMin, productCategory, true);
        List<ProductDTO> dtoList = new LinkedList<>();
        dtoList.add(dto);
        return Response.createResponse(false, objectMapper.writeValueAsString(dtoList));
    }

    public Response searchProductInStore(int storeId, String productName, double productPriceMin, double productPriceMax, String productCategory, int productRating) throws JsonProcessingException {
        ProductDTO dto = new ProductDTO(0, productName, productPriceMin, productCategory, true);
        List<ProductDTO> dtoList = new LinkedList<>();
        dtoList.add(dto);
        return Response.createResponse(false, objectMapper.writeValueAsString(dtoList));
    }

    public Response addProductToBasketGuest(String uuid, int storeId, int productId, int amount){
        return Response.createResponse(false, "true");
    }

    public Response addProductToBasketMember(int userId, int storeId, int productId, int amount){
        return Response.createResponse(false, "true");
    }

    public Response getGuestBasket(String uuid, int storeId) throws JsonProcessingException {
        Map<Integer, Integer> productToAmount = new HashMap<>();
        productToAmount.put(0, 1);
        return Response.createResponse(false, objectMapper.writeValueAsString(productToAmount));
    }

    public Response getMemberBasket(String token, String userId, int storeId) throws JsonProcessingException {
        Map<Integer, Integer> productToAmount = new HashMap<>();
        productToAmount.put(0, 1);
        return Response.createResponse(false, objectMapper.writeValueAsString(productToAmount));
    }

    public Response getGuestCart(String uuid) throws JsonProcessingException {
        Map<Integer, Integer> productToAmount = new HashMap<>();
        productToAmount.put(0, 1);
        Map<Integer, Map<Integer, Integer>> storeToBasket = new HashMap<>();
        storeToBasket.put(0, productToAmount);
        return Response.createResponse(false, objectMapper.writeValueAsString(storeToBasket));
    }

    public Response getMemberCart(String token, String userId) throws JsonProcessingException {
        Map<Integer, Integer> productToAmount = new HashMap<>();
        productToAmount.put(0, 1);
        Map<Integer, Map<Integer, Integer>> storeToBasket = new HashMap<>();
        storeToBasket.put(0, productToAmount);
        return Response.createResponse(false, objectMapper.writeValueAsString(storeToBasket));
    }

    public Response buyCartGuest(String uuid, CreditCardDTO creditDetails, AddressDTO address) {
        return Response.createResponse(false, "4"); //returns orderId
    }

    public Response buyCartMember(String token, String userId, CreditCardDTO creditDetails) {
        return Response.createResponse(false, "4"); //returns orderId
    }

    public Response logout(String token, int userId) {
        return Response.createResponse(false, "true");
    }

    public Response openStore(String token, String userId, String storeName) {
        return Response.createResponse(false, "4"); //returns store id
    }

    public Response addProductToStore(String token, String userId, int storeId, ProductDTO productDetails) {
        return Response.createResponse(false, "5"); //returns product id
    }

    public Response removeProductFromStore(String token, String userId, int storeId, int productId) {
        return Response.createResponse(false, "true");
    }

    public Response editStoreProduct(String token, String userId, int storeId, int productId, ProductDTO productDetails) {
        return Response.createResponse(false, "true");
    }

    public Response appointOwner(String token, String userId, String appointedUserId) {
        return Response.createResponse(false, "true");
    }

    public Response appointManager(String token, String userId, String appointedUserId, List<Integer> permissions) {
        return Response.createResponse(false, "true");
    }

    public Response acceptOwnerAppointment(String token, String appointedUserId, String appointerid) {
        return Response.createResponse(false, "true");
    }

    public Response acceptManagerAppointment(String token, String appointedUserId, String appointerid) {
        return Response.createResponse(false, "true");
    }

    public Response changeManagerPermissions(String token, String userId, String managerId, List<Integer> newPermissions) {
        return Response.createResponse(false, "true");
    }

    public Response closeStore(String token, String userId, int storeId) {
        return Response.createResponse(false, "true");
    }

    public Response getIsOwner(String token, String actorId, int storeId, String ownerId) {
        return Response.createResponse(false, "true");
    }

    public Response getIsManager(String token, String actorId, int storeId, String managerId) {
        return Response.createResponse(false, "true");
    }

    public Response getIsFounder(String token, String actorId, int storeId, String founderId) {
        return Response.createResponse(false, "true");
    }

    public Response getStoreOwners(String token, String actorId, int storeId) throws JsonProcessingException {
        List<UserDTO> users = new LinkedList<>();
        return Response.createResponse(false, objectMapper.writeValueAsString(users));
    }

    public Response getStoreManagers(String token, String actorId, int storeId) throws JsonProcessingException {
        List<UserDTO> users = new LinkedList<>();
        return Response.createResponse(false, objectMapper.writeValueAsString(users));
    }

    public Response getManagerPermissions(String token, String actorId, int storeId, String managerId) throws JsonProcessingException {
        List<Integer> permissions = new LinkedList<>();
        return Response.createResponse(false, objectMapper.writeValueAsString(permissions));
    }

    public Response getStorePurchaseHistory(String token, String actorId, int storeId) throws JsonProcessingException {
        List<OrderDTO> history = new LinkedList<>();
        return Response.createResponse(false, objectMapper.writeValueAsString(history));
    }

    public Response getUserPurchaseHistory(String token, String actorId, String userId) throws JsonProcessingException {
        List<OrderDTO> history = new LinkedList<>();
        return Response.createResponse(false, objectMapper.writeValueAsString(history));
    }
}
