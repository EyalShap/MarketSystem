package com.sadna.sadnamarket.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.service.MarketService;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.*;
import org.apache.commons.logging.Log;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*") // Allow cross-origin requests from any source
public class UserRestController {
    MarketService marketService = MarketService.getInstance();

    @PostMapping("/enterAsGuest")
    public Response enterAsGuest() {
        return marketService.enterAsGuest();
    }

    @PostMapping("/exitGuest")
    public Response exitGuest(@RequestParam int guestId) {
        return marketService.exitGuest(guestId);
    }


    @GetMapping("/isExist")
    public Response isExist(@RequestParam String username,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.memberExists(username);
    }

    @PatchMapping("/setSystemAdminstor")
    public Response setSystemAdminstor(@RequestParam String username,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.setSystemAdminstor(username);
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        return marketService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PatchMapping("/addProductToCart")
    public Response addProductToCart(@RequestParam String username,@RequestBody ProductRequest productRequest ,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.addProductToCart(username,productRequest.storeId,productRequest.productId,productRequest.amount);
    }

    @PatchMapping("/guest/addProductToCart")
    public Response addProductToCartForGuest(@RequestParam int guestId,@RequestBody ProductRequest productRequest) {
        return marketService.addProductToCart(guestId,productRequest.storeId,productRequest.productId,productRequest.amount);
    }
    @PatchMapping("/removeProductFromCart")
    public Response removeProductFromCart(@RequestParam String username,@RequestBody ProductRequest productRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.removeProductFromCart(username,productRequest.getStoreId(),productRequest.getProductId());
    }

    @PatchMapping("/guest/removeProductFromCart")
    public Response removeProductFromCartForGuest(@RequestParam int guestId,@RequestBody ProductRequest productRequest) {
        return marketService.removeProductFromCart(guestId,productRequest.getStoreId(),productRequest.getProductId());
    }

    @PatchMapping("/changeQuantityCart")
    public Response changeQuantityCart(@RequestParam String username, @RequestBody ProductRequest productRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.changeQuantityCart(username,productRequest.getStoreId(),productRequest.getProductId(), productRequest.getAmount());
    }

    @PatchMapping("/guest/changeQuantityCart")
    public Response changeQuantityCartForGusts(@RequestParam int guestId, @RequestBody ProductRequest productRequest) {
        return marketService.changeQuantityCart(guestId,productRequest.getStoreId(),productRequest.getProductId(), productRequest.getAmount());
    }

    @PostMapping("/token/acceptRequest")
    public Response acceptRequestByToken(@RequestParam String newUsername,@RequestParam int storeId,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        return marketService.acceptRequest(token,newUsername,storeId);
    }

    @PostMapping("/acceptRequest")
    public Response acceptRequest(@RequestParam String acceptingName,@RequestParam int requestID,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
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
    public Response leaveRole(@RequestParam String username,@RequestParam int firstName,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.leaveRole(username,firstName);
    }

    @PatchMapping("/setFirstName")
    public Response setFirstName(@RequestParam String username, @RequestParam String firstName,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.setFirstName(username, firstName);
    }

    @PatchMapping("/setLastName")
    public Response setLastName(@RequestParam String username, @RequestParam String lastName,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.setLastName(username, lastName);
    }

    @PatchMapping("/setEmailAddress")
    public Response setEmailAddress(@RequestParam String username, @RequestParam String emailAddress,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.setEmailAddress(username, emailAddress);
    }

    @PatchMapping("/setPhoneNumber")
    public Response setPhoneNumber(@RequestParam String username, @RequestParam String phoneNumber,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.setPhoneNumber(username, phoneNumber);
    }

    @GetMapping("/getOrderHistory")
    public Response getOrderHistory(@RequestParam String username,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.getOrderHistory(username);
    }

    @GetMapping("/getOrderDTOHistory")
    public Response getOrderDTOHistory(@RequestParam String username,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.getOrderDTOHistory(username);
    }

    @GetMapping("/viewCart")
    public Response viewCart(@RequestParam String username,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.viewCart(username);
    }

    @GetMapping("/guest/viewCart")
    public Response viewCartForGuest(@RequestParam int guestId) {
        return marketService.viewCart(guestId);
    }

    @PostMapping("/purchaseCart")
    public Response purchaseCart(@RequestParam String username,@RequestBody CartRequest cartRequest,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.purchaseCart(username,cartRequest.getCreditCard(),cartRequest.getAddressDTO());
    }

    @PostMapping("/guest/purchaseCart")
    public Response purchaseCartForGuest(@RequestParam int guestId,@RequestBody CartRequest cartRequest) {
        return marketService.purchaseCart(guestId,cartRequest.getCreditCard(),cartRequest.getAddressDTO());
    }

    @GetMapping("/getAllOrderDTOHistory")
    public Response getAllOrderDTOHistory(@RequestParam String username,HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Skip "Bearer " prefix
        }
        marketService.checkToken(token,username);
        return marketService.getAllOrderDTOHistory(username);
    }

    @GetMapping("/getUserCart")
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
