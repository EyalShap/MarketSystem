package com.sadna.sadnamarket.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.service.MarketService;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*") // Allow cross-origin requests from any source
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
    public Response isExist(@RequestParam String token,@RequestParam String username) {
        marketService.checkToken(token,username);
        return marketService.memberExists(username);
    }


    @PostMapping("/setSystemAdminstor")
    public Response setSystemAdminstor(@RequestParam String token,@RequestParam String username) {
        marketService.checkToken(token,username);
        return marketService.setSystemAdminstor(username);
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        return marketService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/addProductToCart")
    public Response addProductToCart(@RequestParam String token,@RequestParam String username, @RequestParam int storeId, @RequestParam int productId, @RequestParam int amount) {
        marketService.checkToken(token,username);
        return marketService.addProductToCart(username,storeId,productId,amount);
    }

    @PostMapping("/guest/addProductToCart")
    public Response addProductToCartForGuest(@RequestParam int guestId, @RequestParam int storeId, @RequestParam int productId, @RequestParam int amount) {
        return marketService.addProductToCart(guestId,storeId,productId,amount);
    }
    @PostMapping("/removeProductFromCart")
    public Response removeProductFromCart(@RequestParam String token,@RequestParam String username,@RequestParam int storeId,@RequestParam int productId) {
        marketService.checkToken(token,username);
        return marketService.removeProductFromCart(username,storeId,productId);
    }

    @PostMapping("/guest/removeProductFromCart")
    public Response removeProductFromCartForGuest(@RequestParam int guestId,@RequestParam int storeId,@RequestParam int productId) {
        return marketService.removeProductFromCart(guestId,storeId,productId);
    }

    @PostMapping("/changeQuantityCart")
    public Response changeQuantityCart(@RequestParam String token,@RequestParam String username, @RequestParam int storeId,@RequestParam int productId,@RequestParam int amount) {
        marketService.checkToken(token,username);
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
    public Response acceptRequest(@RequestParam String token,@RequestParam String acceptingName,@RequestParam int requestID) {
        marketService.checkToken(token,acceptingName);
        return marketService.acceptRequest(acceptingName,requestID);
    }

    @PostMapping("/logout")
    public Response logout(@RequestParam String username) {
        return marketService.logout(username);
    }

    @PostMapping("/register")
        public Response register(@RequestBody RegisterRequest registerRequest) {
        
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa " + registerRequest.getBirthDate());
            LocalDate birthDate = LocalDate.parse(registerRequest.getBirthDate(), formatter);
            return marketService.register(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmailAddress(),
                registerRequest.getPhoneNumber(),
                birthDate
            );
    }
    
    @PostMapping("/leaveRole")
    public Response leaveRole(@RequestParam String token,@RequestParam String username,@RequestParam int firstName) {
        marketService.checkToken(token,username);
        return marketService.leaveRole(username,firstName);
    }

    @PostMapping("/setFirstName")
    public Response setFirstName(@RequestParam String token,@RequestParam String username, @RequestParam String firstName) {
        marketService.checkToken(token,username);
        return marketService.setFirstName(username, firstName);
    }

    @PostMapping("/setLastName")
    public Response setLastName(@RequestParam String token,@RequestParam String username, @RequestParam String lastName) {
        marketService.checkToken(token,username);
        return marketService.setLastName(username, lastName);
    }

    @PostMapping("/setEmailAddress")
    public Response setEmailAddress(@RequestParam String token,@RequestParam String username, @RequestParam String emailAddress) {
        marketService.checkToken(token,username);
        return marketService.setEmailAddress(username, emailAddress);
    }

    @PostMapping("/setPhoneNumber")
    public Response setPhoneNumber(@RequestParam String token,@RequestParam String username, @RequestParam String phoneNumber) {
        marketService.checkToken(token,username);
        return marketService.setPhoneNumber(username, phoneNumber);
    }

    @PostMapping("/getOrderHistory")
    public Response getOrderHistory(@RequestParam String token,@RequestParam String username) {
        marketService.checkToken(token,username);
        return marketService.getOrderHistory(username);
    }

    @PostMapping("/getOrderDTOHistory")
    public Response getOrderDTOHistory(@RequestParam String token,@RequestParam String username) {
        marketService.checkToken(token,username);
        return marketService.getOrderDTOHistory(username);
    }

    @PostMapping("/viewCart")
    public Response viewCart(@RequestParam String token,@RequestParam String username) {
        marketService.checkToken(token,username);
        return marketService.viewCart(username);
    }

    @PostMapping("/guest/viewCart")
    public Response viewCartForGuest(@RequestParam int guestId) {
        return marketService.viewCart(guestId);
    }

    @PostMapping("/purchaseCart")
    public Response purchaseCart(@RequestParam String token,@RequestParam String username, @RequestParam CreditCardDTO creditCard, @RequestParam AddressDTO addressDTO) {
        marketService.checkToken(token,username);
        return marketService.purchaseCart(username,creditCard,addressDTO);
    }

    @PostMapping("/guest/purchaseCart")
    public Response purchaseCartForGuest(@RequestParam int guestId, @RequestParam CreditCardDTO creditCard, @RequestParam AddressDTO addressDTO) {
        return marketService.purchaseCart(guestId,creditCard,addressDTO);
    }

    @PostMapping("/getAllOrderDTOHistory")
    public Response getAllOrderDTOHistory(@RequestParam String token,@RequestParam String username) {
        marketService.checkToken(token,username);
        return marketService.getAllOrderDTOHistory(username);
    }

    @PostMapping("/getUserCart")
    public Response getUserCart(@RequestParam int guestId) {
        return marketService.getUserCart(guestId);
    }
    @GetMapping("/getUserDTO/username")
    public Response getMethodName(@RequestParam String username,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }   
        marketService.checkToken(token,username);
        return marketService.getMemberDto(username);
    }
    

}
