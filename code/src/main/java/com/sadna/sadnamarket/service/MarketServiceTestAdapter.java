package com.sadna.sadnamarket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.api.Response;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.stores.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class MarketServiceTestAdapter {
    //this is an adapter for the tests
    //please add the true configuration here when you make additions to the actual service
    //thanks
    @Autowired
    MarketService real;

    ObjectMapper objectMapper = new ObjectMapper();

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
        return Response.createResponse(false, "{\"userId\": \"4\", \"token\": \"3424234\"}");
    }

    public Response memberExists(int userId){
        return Response.createResponse(false, "true");
    }

    public Response tokenValid(String token){
        return Response.createResponse(false, "true");
    }

    public Response login(String email, String passwordHash){
        return Response.createResponse(false, "{\"userId\": \"4\", \"token\": \"3424234\"}");
    }

    public Response logout(String token){
        return Response.createResponse(false, "true");
    }

    public Response getStoreData(String token, int userId, int storeId) throws JsonProcessingException {
        //empty token and -1 userId if guest
        StoreDTO dto = new StoreDTO(4,true,"TestStore", new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        return Response.createResponse(false, objectMapper.writeValueAsString(dto));
    }

    public Response getProductData(String token, int userId, int productId) throws JsonProcessingException {
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

    public Response getMemberBasket(String token, int userId, int storeId) throws JsonProcessingException {
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

    public Response getMemberCart(String token, int userId) throws JsonProcessingException {
        Map<Integer, Integer> productToAmount = new HashMap<>();
        productToAmount.put(0, 1);
        Map<Integer, Map<Integer, Integer>> storeToBasket = new HashMap<>();
        storeToBasket.put(0, productToAmount);
        return Response.createResponse(false, objectMapper.writeValueAsString(storeToBasket));
    }

    public Response buyCartGuest(String uuid) {
        return Response.createResponse(false, "4"); //returns orderId
    }

    public Response buyCartMember(String token, int userId) {
        return Response.createResponse(false, "4"); //returns orderId
    }

    public Response logout(String token, int userId) {
        return Response.createResponse(false, "true");
    }

    public Response openStore(String token, int userId, String storeName) {
        return Response.createResponse(false, "4"); //returns store id
    }

    public Response addProductToStore(String token, int userId, int storeId, ProductDTO productDetails) {
        return Response.createResponse(false, "5"); //returns product id
    }

    public Response removeProductFromStore(String token, int userId, int storeId, int productId) {
        return Response.createResponse(false, "true");
    }

    public Response editStoreProduct(String token, int userId, int storeId, int productId, ProductDTO productDetails) {
        return Response.createResponse(false, "true");
    }

    public Response appointOwner(String token, int userId, int appointedUserId) {
        return Response.createResponse(false, "true");
    }

    public Response appointManager(String token, int userId, int appointedUserId, List<Integer> permissions) {
        return Response.createResponse(false, "true");
    }

    public Response acceptOwnerAppointment(String token, int appointedUserId, int appointerid) {
        return Response.createResponse(false, "true");
    }

    public Response acceptManagerAppointment(String token, int appointedUserId, int appointerid) {
        return Response.createResponse(false, "true");
    }

    public Response changeManagerPermissions(String token, int userId, int managerId, List<Integer> newPermissions) {
        return Response.createResponse(false, "true");
    }

    public Response closeStore(String token, int userId, int storeId) {
        return Response.createResponse(false, "true");
    }

    public Response getIsOwner(String token, int actorId, int storeId, int ownerId) {
        return Response.createResponse(false, "true");
    }

    public Response getIsManager(String token, int actorId, int storeId, int managerId) {
        return Response.createResponse(false, "true");
    }

    public Response getIsFounder(String token, int actorId, int storeId, int founderId) {
        return Response.createResponse(false, "true");
    }

    public Response getManagerPermissions(String token, int actorId, int storeId, int managerId) throws JsonProcessingException {
        List<Integer> permissions = new LinkedList<>();
        return Response.createResponse(false, objectMapper.writeValueAsString(permissions));
    }

    public Response getStorePurchaseHistory(String token, int actorId, int storeId) throws JsonProcessingException {
        List<OrderDTO> history = new LinkedList<>();
        return Response.createResponse(false, objectMapper.writeValueAsString(history));
    }

    public Response getUserPurchaseHistory(String token, int actorId, int userId) throws JsonProcessingException {
        List<OrderDTO> history = new LinkedList<>();
        return Response.createResponse(false, objectMapper.writeValueAsString(history));
    }
}
