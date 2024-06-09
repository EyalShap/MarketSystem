package com.sadna.sadnamarket.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.service.MarketService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    MarketService marketService = MarketService.getInstance();

    //Invoke-WebRequest -Uri "http://localhost:8080/api/user/enterAsGuest""
    @PostMapping("/enterAsGuest")
    public Response enterAsGuest() {
        return marketService.enterAsGuest();
    }

    //Invoke-WebRequest -Uri "http://localhost:8080/api/user/exitGuest""
    @PostMapping("/exitGuest")
    public Response exitGuest(@RequestParam int guestId) {
        return marketService.exitGuest(guestId);
    }


    //Invoke-WebRequest -Uri "http://localhost:8080/api/user/isExist""
    @PostMapping("/isExist")
    public Response isExist(@RequestParam String username) {
        return marketService.memberExists(username);
    }


    @PostMapping("/setSystemAdminstor")
    public Response setSystemAdminstor(@RequestParam String username) {
        return marketService.setSystemAdminstor(username);
    }

    @PostMapping("/login")
    public Response login(@RequestParam String username, @RequestParam String password) {
        return marketService.login(username, password);
    }

    @PostMapping("/addProductToCart")
    public Response addProductToCart(@RequestParam String username, @RequestParam int storeId, @RequestParam int productId, @RequestParam int amount) {
        return marketService.addProductToCart(username,storeId,productId,amount);
    }

    @PostMapping("/guest/addProductToCart")
    public Response addProductToCartForGuest(@RequestParam int guestId, @RequestParam int storeId, @RequestParam int productId, @RequestParam int amount) {
        return marketService.addProductToCart(guestId,storeId,productId,amount);
    }
    @PostMapping("/removeProductFromCart")
    public Response removeProductFromCart(@RequestParam String username,@RequestParam int storeId,@RequestParam int productId) {
        return marketService.removeProductFromCart(username,storeId,productId);
    }

    @PostMapping("/guest/removeProductFromCart")
    public Response removeProductFromCartForGuest(@RequestParam int guestId,@RequestParam int storeId,@RequestParam int productId) {
        return marketService.removeProductFromCart(guestId,storeId,productId);
    }

    @PostMapping("/changeQuantityCart")
    public Response changeQuantityCart(@RequestParam String username, @RequestParam int storeId,@RequestParam int productId,@RequestParam int amount) {
        return marketService.changeQuantityCart(username,storeId,productId, amount);
    }

    @PostMapping("/guest/changeQuantityCart")
    public Response changeQuantityCartForGusts(@RequestParam int guestId, @RequestParam int storeId,@RequestParam int productId,@RequestParam int amount) {
        return marketService.changeQuantityCart(guestId,storeId,productId, amount);
    }

    @PostMapping("/token/acceptRequest")
    public Response acceptRequestByToken(@RequestParam String token,@RequestParam String newUsername,@RequestParam int storeId) {
        return marketService.acceptRequest(token,newUsername,storeId);
    }

    @PostMapping("/acceptRequest")
    public Response acceptRequest(@RequestParam String acceptingName,@RequestParam int requestID) {
        return marketService.acceptRequest(acceptingName,requestID);
    }

    @PostMapping("/logout")
    public Response logout(@RequestParam String username) {
        return marketService.logout(username);
    }

    @PostMapping("/register")
        public Response register(@RequestParam String username, @RequestParam String password, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String emailAddress, @RequestParam String phoneNumber) {
        return marketService.register(username, password, firstName,lastName,emailAddress,phoneNumber);
    }

    @PostMapping("/leaveRole")
    public Response leaveRole(@RequestParam String username,@RequestParam int firstName) {
        return marketService.leaveRole(username,firstName);
    }

    @PostMapping("/setFirstName")
    public Response setFirstName(@RequestParam String username, @RequestParam String firstName) {
        return marketService.setFirstName(username, firstName);
    }

    @PostMapping("/setLastName")
    public Response setLastName(@RequestParam String username, @RequestParam String lastName) {
        return marketService.setLastName(username, lastName);
    }

    @PostMapping("/setEmailAddress")
    public Response setEmailAddress(@RequestParam String username, @RequestParam String emailAddress) {
        return marketService.setEmailAddress(username, emailAddress);
    }

    @PostMapping("/setPhoneNumber")
    public Response setPhoneNumber(@RequestParam String username, @RequestParam String phoneNumber) {
        return marketService.setPhoneNumber(username, phoneNumber);
    }

    @PostMapping("/getOrderHistory")
    public Response getOrderHistory(@RequestParam String username) {
        return marketService.getOrderHistory(username);
    }

    @PostMapping("/getOrderDTOHistory")
    public Response getOrderDTOHistory(@RequestParam String username) {
        return marketService.getOrderDTOHistory(username);
    }

    @PostMapping("/viewCart")
    public Response viewCart(@RequestParam String username) {
        return marketService.viewCart(username);
    }

    @PostMapping("/guest/viewCart")
    public Response viewCartForGuest(@RequestParam int guestId) {
        return marketService.viewCart(guestId);
    }

    @PostMapping("/purchaseCart")
    public Response purchaseCart(@RequestParam String username, @RequestParam CreditCardDTO creditCard, @RequestParam AddressDTO addressDTO) {
        return marketService.purchaseCart(username,creditCard,addressDTO);
    }

    @PostMapping("/guest/purchaseCart")
    public Response purchaseCartForGuest(@RequestParam int guestId, @RequestParam CreditCardDTO creditCard, @RequestParam AddressDTO addressDTO) {
        return marketService.purchaseCart(guestId,creditCard,addressDTO);
    }

    @PostMapping("/getAllOrderDTOHistory")
    public Response getAllOrderDTOHistory(@RequestParam String username) {
        return marketService.getAllOrderDTOHistory(username);
    }

    @PostMapping("/getUserCart")
    public Response getUserCart(@RequestParam int guestId) {
        return marketService.getUserCart(guestId);
    }
    

}
