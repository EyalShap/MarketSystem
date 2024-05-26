package com.sadna.sadnamarket.domain.users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.orders.OrderDTO;
import com.sadna.sadnamarket.domain.orders.OrderFacade;
import com.sadna.sadnamarket.domain.payment.CreditCardDTO;
import com.sadna.sadnamarket.domain.payment.PaymentService;
import com.sadna.sadnamarket.domain.stores.StoreFacade;
import com.sadna.sadnamarket.domain.supply.AddressDTO;
import com.sadna.sadnamarket.domain.supply.OrderDetailsDTO;
import com.sadna.sadnamarket.domain.supply.SupplyService;



public class UserFacade {
    private IUserRepository iUserRepo; 
    private static String systemManagerUserName;
    private static StoreFacade storeFacade;
    private static OrderFacade orderFacade;
    private static final Logger logger = LogManager.getLogger(UserFacade.class);

    public UserFacade(IUserRepository userRepo, StoreFacade storeFacadeInstance,OrderFacade orderFacadeInsance) {
        logger.info("initilize user fascade");
        this.iUserRepo=userRepo;
        systemManagerUserName=null;
        storeFacade=storeFacadeInstance;
        orderFacade=orderFacadeInsance;
        logger.info("finish initilize user fascade");
    }
    
    public synchronized int enterAsGuest(){
        logger.info("enter as guest");
       return iUserRepo.addGuest();
    }

    public synchronized void exitGuest(int guestId){
        logger.info("remove guest {}",guestId);
        iUserRepo.deleteGuest(guestId);
        logger.info("removed guest {}",guestId);
    }

    public boolean checkPremssionToStore(String userName, int storeId,Permission permission){
        logger.info("check permission to {} for user {} for store {}",permission.getValue(),userName,storeId);
        Member member=iUserRepo.getMember(userName);
        boolean isAuthrized=member.hasPermissionToRole(permission,storeId);
        logger.info("checked permission to {} for user {} for store {} and answer is: {}",permission.getValue(),userName,storeId,isAuthrized);
        return isAuthrized ;
    }

    public void notify(String userName, String msg) {
        logger.info("{} got notification {}",userName,msg);
        iUserRepo.getMember(userName).addNotification(msg);
    }

    public List<NotificationDTO> getNotifications(String username){
        logger.info("getting notifications for {}",username);
        List<Notification> notifes = new ArrayList<>(iUserRepo.getMember(username).getNotifications().values());
        List<NotificationDTO> notificationDTOs=new ArrayList<NotificationDTO>();
        for(Notification notif : notifes){
            notificationDTOs.add(notif.toDTO());
        }
        logger.info("got notifications for {}",username);
        return notificationDTOs;
    }


    public boolean isLoggedIn(String username){
        logger.info("check for login for member {}",username);
        if(isExist(username)){
            Member member= iUserRepo.getMember(username);
            logger.info("checked for login for member {}",username);
            return member.isLoggedIn();
        }
        logger.info("check for login for member but member doesnt exist {}",username);
        return false;
    }

    public boolean isExist(String userName){
        logger.info("check if member exist {}",userName);
        return iUserRepo.hasMember(userName);
    }

    public void setSystemManagerUserName(String username){
        logger.info("set system username {}",username);
        if(!isExist(username)){
            logger.error("user doesnt exist",username);
            throw new IllegalStateException("only registered user can be system manager");
        }
        systemManagerUserName=username;
        logger.info("done set system username {}",username);
    }
    public String getSystemManagerUserName(){
        logger.info("get system username {}",systemManagerUserName);
        return systemManagerUserName;
    }
    public void login(String userName,String password){
        logger.info("{} tries to login",userName);
        isValid(userName);
        if(iUserRepo.getMember(userName).isLoggedIn()){
            logger.error("user {} already logged in",userName);
            throw new IllegalStateException("user already logged in");
        }
        iUserRepo.getMember(userName).setLogin(true);
        logger.info("{} done login",userName);
    }
    public void addProductToCart(String username,int storeId, int productId,int amount){
        logger.info("{} add prooduct {} from store id {} amount: {}",username,productId,storeId,amount);
        if(amount<=0)
            throw new IllegalArgumentException("amount should be above 0");
        if(!storeFacade.hasProductInStock(storeId, productId, amount))
            throw new IllegalArgumentException("Amount doesn't exist in store");
        iUserRepo.getMember(username).addProductToCart(storeId, productId, amount);
        logger.info("{} added prooduct {} from store id {} amount: {}",username,productId,storeId,amount);
    }
    public void addProductToCart(int guestId,int storeId, int productId,int amount){
        logger.info("guest: {} add prooduct {} from store id {} amount: {}",guestId,productId,storeId,amount);
        if(amount<=0)
            throw new IllegalArgumentException("amount should be above 0");
        if(!storeFacade.hasProductInStock(storeId, productId, amount))
            throw new IllegalArgumentException("Amount doesn't exist in store");
        iUserRepo.getGuest(guestId).addProductToCart(storeId, productId, amount);
        logger.info("guest: {} add prooduct {} from store id {} amount: {}",guestId,productId,storeId,amount);

    }
    public void removeProductFromCart(String username,int storeId, int productId){
        logger.info("{} remove prooduct {} from store id {}",username,productId,storeId);
        iUserRepo.getMember(username).removeProductFromCart(storeId, productId);
        logger.info("{} removed prooduct {} from store id {}",username,productId,storeId);
    }
    public void removeProductFromCart(int guestId,int storeId, int productId){
        logger.info("guest {} removed prooduct {} from store id {}",guestId,productId,storeId);
        iUserRepo.getGuest(guestId).removeProductFromCart(storeId, productId);
        logger.info("guest {} removed prooduct {} from store id {}",guestId,productId,storeId);
    }
    public void changeQuantityCart(String username,int storeId, int productId,int amount){
        logger.info("{} try to change amount of prooduct {} from store id {} amount: {}",username,productId,storeId,amount);
        if(amount<=0)
            throw new IllegalArgumentException("amount should be above 0");
        if(!storeFacade.hasProductInStock(storeId, productId, amount))
            throw new IllegalArgumentException("Amount doesn't exist in store");
        iUserRepo.getMember(username).changeQuantityCart(storeId, productId, amount);
        logger.info("{} changed amount of prooduct {} from store id {} amount: {}",username,productId,storeId,amount);

    }
    public void changeQuantityCart(int guestId,int storeId, int productId,int amount){
        logger.info("guest {} try to change amount of prooduct {} from store id {} amount: {}",guestId,productId,storeId,amount);
        if(amount<=0)
            throw new IllegalArgumentException("amount should be above 0");
        if(!storeFacade.hasProductInStock(storeId, productId, amount))
            throw new IllegalArgumentException("Amount doesn't exist in store");
        iUserRepo.getGuest(guestId).changeQuantityCart(storeId, productId, amount);
        logger.info("guest: {} try to changed amount of prooduct {} from store id {} amount: {}",guestId,productId,storeId,amount);

    }
    public Member getMember(String userName){
        logger.info("try to get member {}",userName);
        return iUserRepo.getMember(userName);
    }    
    
    public void addOwnerRequest(String senderName,String userName,int store_id){
        logger.info("{} try to add owner request to {} for store {}",senderName,userName,store_id);
        Member sender=getMember(senderName);
        sender.addOwnerRequest(this,userName, store_id);
        logger.info("{} added owner request to {} for store {}",senderName,userName,store_id);

    }
    public void addManagerRequest(String senderName,String userName,int store_id){
        logger.info("{} try to add manager request to {} for store {}",senderName,userName,store_id);
        Member sender=getMember(senderName);
        sender.addManagerRequest(this,userName, store_id);
        logger.info("{} added manager request to {} for store {}",senderName,userName,store_id);

    }

    public void accept(String acceptingName,int requestID){
        logger.info("{} accept request id: {}",acceptingName,requestID);
        Member accepting=getMember(acceptingName);
        Request request=accepting.getRequest(requestID);
        int storeId=request.getStoreId();
        accepting.accept(requestID);
        String role=request.getRole();
        String apointer=request.getSender();
        iUserRepo.getMember(apointer).addApointer(acceptingName, storeId);
        if(role.equals("Manager"))
            storeFacade.addStoreManager(acceptingName, storeId);
        else
            storeFacade.addStoreOwner(acceptingName,storeId);
        logger.info("{} accepted request id: {}",acceptingName,requestID);
    }

    public void reject(String rejectingName,int requestID){
        logger.info("{} reject request id: {}",rejectingName,requestID);
        Member rejecting=getMember(rejectingName);
        rejecting.accept(requestID);
        logger.info("{} rejected request id: {}",rejectingName,requestID);

    }

    public void login(String userName,String password, int guestId){//the cart of the guest
        logger.info("{} login from guest {}",userName,guestId);
        isValid(userName);
        Member member=iUserRepo.getMember(userName);
        if(member.getCart().isEmpty()){
            member.setCart(iUserRepo.getGuest(guestId).getCart());
        }
        member.setLogin(true);
        iUserRepo.deleteGuest(guestId);
        logger.info("{} done login from guest {}",userName,guestId);
    }
    
    public int logout(String userName){
        logger.info("{} logout ",userName);
        if(!iUserRepo.hasMember(userName))
            throw new NoSuchElementException("User doesnt exist in system");

        iUserRepo.getMember(userName).logout();
        logger.info("{} done logout",userName);
        return enterAsGuest();
    }
    public void setCart(Cart cart,String userName){
        logger.info("{} set cart ",userName);
        iUserRepo.getMember(userName).setCart(cart);
        logger.info("{} done set cart ",userName);
    }
  

    public void register(String username,String firstName, String lastName,String emailAddress,String phoneNumber){
        logger.info("{} try to register ",username);

        Member member=new Member(username,firstName,lastName,emailAddress,phoneNumber);
        iUserRepo.store(member);
        logger.info("{} done register ",username);
    }

   
    public void addStoreManager(String username,int storeId){
        logger.info("add Store Manager to {} in {} ",username,storeId);
        iUserRepo.getMember(username).addRole(new StoreManager(storeId));
        logger.info("done add Store Manager to {} in {} ",username,storeId);

    }
    public void addStoreOwner(String username,String asignee, int storeId){
        logger.info("add Store owner to {} in {} ",username,storeId);
        iUserRepo.getMember(username).addRole(new StoreOwner(storeId,asignee));
        logger.info("done add Store owner to {} in {} ",username,storeId);

    }
    public void addStoreFounder(String username,int storeId){
        logger.info("add Store founder to {} in {} ",username,storeId);
        iUserRepo.getMember(username).addRole(new StoreFounder(storeId,username));
        logger.info("done add Store founder to {} in {} ",username,storeId);
    }
    public void addPremssionToStore(String giverUserName,String userName, int storeId,Permission permission){
        logger.info("{} get permission to store {} to {}",userName,storeId,permission);
        if(!iUserRepo.getMember(giverUserName).getRoleOfStore(storeId).getAppointers().contains(userName))
            throw new IllegalStateException("you can add permissions only to your appointers");
        Member member=iUserRepo.getMember(userName);
        member.addPermissionToRole(permission, storeId);
        logger.info("{} got permission to store {} to {}",userName,storeId,permission);

    }
    public void removePremssionFromStore(String removerUsername,String userName, int storeId,Permission permission){
        logger.info("{} remove permission to store {} to {}",userName,storeId,permission);
        if(!iUserRepo.getMember(removerUsername).getRoleOfStore(storeId).getAppointers().contains(userName))
            throw new IllegalStateException("you can add permissions only to your appointers");
        Member member=iUserRepo.getMember(userName);
        member.removePermissionFromRole(permission, storeId);
        logger.info("{} remove permission to store {} to {}",userName,storeId,permission);
    }

    public List<Permission> getManagerPermissions(String actorUsername,String userName, int storeId){
        logger.info("{} got permissions of {} in store {}",actorUsername, userName, storeId);
        Member member=iUserRepo.getMember(userName);
        logger.info("{} got permissions of {} in store {}",actorUsername, userName, storeId);
        return member.getPermissions(storeId);
    }

    public void leaveRole(String username,int storeId){
        logger.info("{} try leave role in store {}",username,storeId);
        Member member=iUserRepo.getMember(username);
        UserRole role=member.getRoleOfStore(storeId);
        role.leaveRole(new UserRoleVisitor(), storeId, member, this);
        member.removeRole(role);
        logger.info("{} try left role in store {}",username,storeId);
    }
    public void removeRoleFromMember(String username,String remover,int storeId){
        Member member=iUserRepo.getMember(username);
        List<UserRole> roles=member.getUserRoles();
        for(UserRole role : roles){
           // role
           if(role.getStoreId()==storeId){
            if(!role.getApointee().equals(remover))
                throw new IllegalStateException("you can only remove your apointees");
            role.leaveRole(new UserRoleVisitor(), storeId, member,this);;
           }
        }
    }
    public void setFirstName(String userName, String firstName) {
        logger.info("set first name for {}", firstName);
        isValid(firstName);
        Member member = iUserRepo.getMember(userName);
        member.setFirstName(firstName);
        logger.info("done set first name for {}", firstName);

    }

    public void setLastName(String userName, String lastName) {
        logger.info("set last name for {}", userName);
        isValid(lastName);
        Member member = iUserRepo.getMember(userName);
        member.setLastName(lastName);
        logger.info("done set last name for {}", userName);

    }

    public void setEmailAddress(String userName, String emailAddress) {
        logger.info("set email for {}", userName);
        isValid(emailAddress);
        Member member = iUserRepo.getMember(userName);
        member.setEmailAddress(emailAddress);
        logger.info("done set email for {}", userName);
    }

    public void setPhoneNumber(String userName, String phoneNumber) {
        logger.info("set phone number for {}", userName);
        isValid(phoneNumber);
        Member member = iUserRepo.getMember(userName);
        member.setPhoneNumber(phoneNumber);
        logger.info("done set phone number for {}", userName);
    }
    private void isValid(String detail){
        logger.info("check if field is valid");
        if(detail==null||detail.trim().equals("")){
            throw new IllegalArgumentException("please enter valid string");
        }
        logger.info("field {} is valid",detail);

    }
    public MemberDTO getMemberDTO(String userName){
        logger.info("get memberDTO for {}",userName);
        isValid(userName);
        Member member=iUserRepo.getMember(userName);
        MemberDTO memberDTO=new MemberDTO(member);
        logger.info("finished get memberDTO for {}",userName);
        return memberDTO;
    }
    public List<Integer> getMemberPermissions(String userName, int storeId){
        logger.info("get permissions for {} in {}",userName,storeId);
        isValid(userName);
        UserRole role=iUserRepo.getMember(userName).getRoleOfStore(storeId);
        List<Integer> permissionsInRole=new ArrayList<>();
        for(Permission permission: Permission.values()){
            if (role.hasPermission(permission))
                permissionsInRole.add(permission.getValue());

        }
        logger.info("got permissions for {} in {}",userName,storeId);
        return permissionsInRole;

    }
    public List<String> getMemberRoles(String userName){
        logger.info("get user roles for {}",userName);
        Member member=iUserRepo.getMember(userName);
        List<String> userRoles=member.getUserRolesString();
        logger.info("got user roles for {}: {}",userName, userRoles);
        return userRoles;

    }

    public List<String> getUserOrders(String username){
        List<Integer> ordersIds=getMember(username).getOrdersHistory();
        List <String> ordersString=new ArrayList<>();
        for (Integer orderId : ordersIds) {
            Map<Integer,OrderDTO> orders=orderFacade.getOrderByOrderId(orderId);
            for (Integer store_id : orders.keySet()) {
                Map<Integer,String> ordersProducts=orders.get(store_id).getOrderProductsJsons();
                for (Integer product_id : ordersProducts.keySet()) {
                    ordersString.add(ordersProducts.get(product_id));
                }
            }
        }
        return ordersString;
    }

    public List<OrderDTO> getUserOrderDTOs(String username){
        logger.info("get orders for {}",username);
        List<Integer> ordersIds=getMember(username).getOrdersHistory();
        List <OrderDTO> orders =new ArrayList<>();
        for (Integer orderId : ordersIds) {
            Map<Integer,OrderDTO> ordersMap =orderFacade.getOrderByOrderId(orderId);
            for (Integer store_id : ordersMap.keySet()) {
                orders.add(ordersMap.get(store_id));
            }
        }
        logger.info("finished get orders for {}",username);
        return orders;
    }


    private double calculateFinalPrice(String username,List<CartItemDTO> items){
        logger.info("calculate final price for user {} with items {}",username,items);
        double sum=0;
        Map<Integer, List<ProductDataPrice>> storeApriceData=storeFacade.calculatePrice(username, items);
        for(List<ProductDataPrice> price: storeApriceData.values()){
            for(ProductDataPrice prod: price){
                sum=+prod.getNewPrice();
            }
        }
        logger.info("finished calculate final price for user {} with items {} and got {}",username,items, sum);
        return sum;
    }
    private double calculateOldPrice(String username,List<CartItemDTO> items){
        logger.info("calculate old price for user {} with items {}",username,items);
        double sum=0;
        Map<Integer, List<ProductDataPrice>> storeApriceData=storeFacade.calculatePrice(username, items);
        for(List<ProductDataPrice> price: storeApriceData.values()){
            for(ProductDataPrice prod: price){
                sum=+prod.getOldPrice();
            }
        }
        logger.info("finished calculate old price for user {} with items {} and got {}",username,items, sum);
        return sum;

    }

    public UserOrderDTO viewCart(String username) throws Exception {
        logger.info("view cart for user {}",username);
        List<CartItemDTO> items=iUserRepo.getUserCart(username);
        storeFacade.checkCart(username, items);
        Map<Integer, List<ProductDataPrice>> storeApriceData=storeFacade.calculatePrice(username, items);
        List<ProductDataPrice> allProudctsData=new ArrayList<ProductDataPrice>();
        for(List<ProductDataPrice> price: storeApriceData.values()){
            for(ProductDataPrice prod: price){
                allProudctsData.add(prod);
            }
        }
        logger.info("finished view cart for user {}",username);
        return new UserOrderDTO(allProudctsData,calculateOldPrice(username, items),calculateFinalPrice(username, items));
        
    }
    public UserOrderDTO viewCart(int guestId) throws Exception {
        List<CartItemDTO> items=iUserRepo.getGuestCart(guestId);
        storeFacade.checkCart(null, items);
        Map<Integer, List<ProductDataPrice>> storeApriceData=storeFacade.calculatePrice(null, items);
        List<ProductDataPrice> allProudctsData=new ArrayList<ProductDataPrice>();
        for(List<ProductDataPrice> price: storeApriceData.values()){
            for(ProductDataPrice prod: price){
                allProudctsData.add(prod);
            }
        }
        return new UserOrderDTO(allProudctsData,calculateOldPrice(null, items),calculateFinalPrice(null, items));

        
    }
    public void purchaseCart(String username,CreditCardDTO creditCard,AddressDTO addressDTO) throws Exception {
        logger.info("purchase cart for user {} with credit card {} and address {}",username,creditCard,addressDTO);
        List<CartItemDTO> items=iUserRepo.getUserCart(username);
        if(creditCard.getDigitsOnTheBack().isEmpty() || creditCard.getCreditCardNumber().isEmpty() || creditCard.getOwnerId().isEmpty()){
            throw new IllegalArgumentException("Missing card details");
        }
        if(addressDTO.getCountry().isEmpty() || addressDTO.getCity().isEmpty() || addressDTO.getAddressLine1().isEmpty()){
            throw new IllegalArgumentException("Missing address details");
        }
        storeFacade.checkCart(username, items);
        // proxy payment
        PaymentService payment = PaymentService.getInstance();
        if(!payment.checkCardValid(creditCard)){
            throw new IllegalArgumentException("Credit card is invalid");
        }
        // update quantities
        storeFacade.buyCart(username, items);
        // create new order 
        Map<Integer,List<ProductDataPrice>> storeBag=storeFacade.calculatePrice(username, items);
        SupplyService supply=SupplyService.getInstance();
        UserOrderDTO order= viewCart(username);
        List<ProductDataPrice> productList= order.getProductsData();
        Map<Integer,Integer> productAmount=new HashMap<>();
        for(ProductDataPrice product:productList){
            productAmount.put(product.getId(),product.getAmount());
        }
        if(!supply.canMakeOrder(new OrderDetailsDTO(productAmount), addressDTO)){
            throw new IllegalStateException("Order cannot be supplied");
        }
        String supplyString = supply.makeOrder(new OrderDetailsDTO(productAmount), addressDTO);
        for(int storeId : storeBag.keySet()){
            double payAmount = 0;
            for(ProductDataPrice price : storeBag.get(storeId)){
                payAmount += price.getNewPrice();
            }
            if(!payment.pay(payAmount, creditCard, storeFacade.getStoreBankAccount(storeId))){
                supply.cancelOrder(supplyString);
                throw new IllegalStateException("Payment could not be completed for store " + storeId);
            }
            Map<Integer,List<ProductDataPrice>> productAmounts=new HashMap<>();
            productAmounts.put(storeId, productList);
            int orderId=orderFacade.createOrder(productAmounts,username);
            iUserRepo.getMember(username).addOrder(orderId);
        }
        logger.info("finish purchase cart for user {} with credit card {} and address {}",username,creditCard,addressDTO);
    }


    public void purchaseCart(int guestId,CreditCardDTO creditCard,AddressDTO addressDTO) throws Exception {
        logger.info("purchase cart for guest {} with credit card {} and address {}",guestId,creditCard,addressDTO);
        List<CartItemDTO> items=iUserRepo.getGuestCart(guestId);
        if(creditCard.getDigitsOnTheBack().isEmpty() || creditCard.getCreditCardNumber().isEmpty() || creditCard.getOwnerId().isEmpty()){
            throw new IllegalArgumentException("Missing card details");
        }
        if(addressDTO.getCountry().isEmpty() || addressDTO.getCity().isEmpty() || addressDTO.getAddressLine1().isEmpty()){
            throw new IllegalArgumentException("Missing address details");
        }
        storeFacade.checkCart(null, items);
         PaymentService payment = PaymentService.getInstance();
        payment.checkCardValid(creditCard);
        // update quantities
        storeFacade.buyCart(null, items);
        // create new order 
        Map<Integer,List<ProductDataPrice>> storeBag=storeFacade.calculatePrice(null, items);
        SupplyService supply=SupplyService.getInstance();
        UserOrderDTO order= viewCart(guestId);
        List<ProductDataPrice> productList= order.getProductsData();
        Map<Integer,Integer> productAmount=new HashMap<>();
        for(ProductDataPrice product:productList){
            productAmount.put(product.getId(),product.getAmount());
        }
        if(!supply.canMakeOrder(new OrderDetailsDTO(productAmount), addressDTO)){
            throw new IllegalStateException("Order cannot be supplied");
        }
        String supplyString = supply.makeOrder(new OrderDetailsDTO(productAmount), addressDTO);
        for(int storeId : storeBag.keySet()){
            double payAmount = 0;
            for(ProductDataPrice price : storeBag.get(storeId)){
                payAmount += price.getNewPrice();
            }
            if(!payment.pay(payAmount, creditCard, storeFacade.getStoreBankAccount(storeId))){
                supply.cancelOrder(supplyString);
                throw new IllegalStateException("Payment could not be completed for store " + storeId);
            }
            Map<Integer,List<ProductDataPrice>> productAmounts=new HashMap<>();
            productAmounts.put(storeId, productList);
            orderFacade.createOrder(productAmounts,null);
        }
        logger.info("finish purchase cart for guest {} with credit card {} and address {}",guestId,creditCard,addressDTO);
    }

    public List<OrderDTO> getAllOrders(String username){
        logger.info("get all orders for {}",username);
        if(!isExist(username))
            throw new NoSuchElementException("User doesnt exist in system");
        if(!iUserRepo.getMember(username).isLoggedIn())
            throw new IllegalStateException("User is not logged in");
        if(!username.equals(systemManagerUserName))
            throw new IllegalStateException("Only system manager can view all orders");
        logger.info("got all orders for {}",username);
        return orderFacade.getAllOrders();
    }

    //only for tests
    public List<CartItemDTO> getCartItems(int guest_id){
        logger.info("get guest cart");
        return iUserRepo.getGuestCart(guest_id);
    }

}
