package com.sadna.sadnamarket.domain.users;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.sadna.sadnamarket.service.Error;
import com.sadna.sadnamarket.service.RealtimeService;
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
    private RealtimeService realtime;

    public UserFacade(RealtimeService realtime, IUserRepository userRepo, StoreFacade storeFacadeInstance,OrderFacade orderFacadeInsance) {
        logger.info("initilize user fascade");
        this.iUserRepo=userRepo;
        this.realtime = realtime;
        systemManagerUserName=null;
        storeFacade=storeFacadeInstance;
        orderFacade=orderFacadeInsance;
        logger.info("finish initilize user fascade");
    }

    public UserFacade(IUserRepository userRepo, StoreFacade storeFacadeInstance,OrderFacade orderFacadeInsance) {
        logger.info("initilize user fascade");
        this.iUserRepo=userRepo;
        this.realtime = null;
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
        NotificationDTO notificationDTO = iUserRepo.getMember(userName).addNotification(msg);
        if(isLoggedIn(userName) && realtime != null){
            realtime.sendNotification(userName, notificationDTO);
        }
        if(isLoggedIn(userName) && realtime == null){
            logger.info("Did not send realtime notification as realtime service is null");
        }
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
            throw new IllegalStateException(Error.makeUserSystemManagerError());
        }
        systemManagerUserName=username;
        logger.info("done set system username {}",username);
    }
    public String getSystemManagerUserName(){
        logger.info("get system username {}",systemManagerUserName);
        return systemManagerUserName;
    }

    public boolean isSystemManager(String username){
        logger.info("check if username is system manager {}",username);
        return username != null && username.equals(systemManagerUserName);
    }

    public void login(String userName,String password){
        logger.info("{} tries to login",userName);
        isValid(userName);
        if(iUserRepo.getMember(userName).isLoggedIn()){
            logger.error("user {} already logged in",userName);
            throw new IllegalStateException(Error.makeUserLoggedInError());
        }
        iUserRepo.getMember(userName).setLogin(true);
        logger.info("{} done login",userName);
    }
    public void addProductToCart(String username,int storeId, int productId,int amount){
        logger.info("{} add prooduct {} from store id {} amount: {}",username,productId,storeId,amount);
        if(amount<=0)
            throw new IllegalArgumentException(Error.makeCartAmountAboveZeroError());
        if(!storeFacade.hasProductInStock(storeId, productId, amount))
            throw new IllegalArgumentException(Error.makeCartAmountDoesntExistError());
        iUserRepo.getMember(username).addProductToCart(storeId, productId, amount);
        logger.info("{} added prooduct {} from store id {} amount: {}",username,productId,storeId,amount);
    }
    public void addProductToCart(int guestId,int storeId, int productId,int amount){
        logger.info("guest: {} add prooduct {} from store id {} amount: {}",guestId,productId,storeId,amount);
        if(amount<=0)
            throw new IllegalArgumentException(Error.makeCartAmountAboveZeroError());
        if(!storeFacade.hasProductInStock(storeId, productId, amount))
            throw new IllegalArgumentException(Error.makeCartAmountDoesntExistError());
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
            throw new IllegalArgumentException(Error.makeCartAmountAboveZeroError());
        if(!storeFacade.hasProductInStock(storeId, productId, amount))
            throw new IllegalArgumentException(Error.makeCartAmountDoesntExistError());
        iUserRepo.getMember(username).changeQuantityCart(storeId, productId, amount);
        logger.info("{} changed amount of prooduct {} from store id {} amount: {}",username,productId,storeId,amount);

    }
    public void changeQuantityCart(int guestId,int storeId, int productId,int amount){
        logger.info("guest {} try to change amount of prooduct {} from store id {} amount: {}",guestId,productId,storeId,amount);
        if(amount<=0)
            throw new IllegalArgumentException(Error.makeCartAmountAboveZeroError());
        if(!storeFacade.hasProductInStock(storeId, productId, amount))
            throw new IllegalArgumentException(Error.makeCartAmountDoesntExistError());
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
        RequestDTO request = sender.addOwnerRequest(this,userName, store_id);
        logger.info("{} added owner request to {} for store {}",senderName,userName,store_id);
        if(isLoggedIn(userName) && realtime != null){
            realtime.sendNotification(userName, request);
        }
        if(isLoggedIn(userName) && realtime == null){
            logger.info("Did not send realtime notification as realtime service is null");
        }
    }
    public void addManagerRequest(String senderName,String userName,int store_id){
        logger.info("{} try to add manager request to {} for store {}",senderName,userName,store_id);
        Member sender=getMember(senderName);
        RequestDTO request = sender.addManagerRequest(this,userName, store_id);
        logger.info("{} added manager request to {} for store {}",senderName,userName,store_id);
        if(isLoggedIn(userName) && realtime != null){
            realtime.sendNotification(userName, request);
        }
        if(isLoggedIn(userName) && realtime == null){
            logger.info("Did not send realtime notification as realtime service is null");
        }
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
        exitGuest(guestId);
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
  

    public void register(String username,String firstName, String lastName,String emailAddress,String phoneNumber, LocalDate birthDate){
        logger.info("{} try to register ",username);

        Member member=new Member(username,firstName,lastName,emailAddress,phoneNumber,birthDate);
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
            throw new IllegalStateException(Error.makeUserCanOnlyEditPermissionsToApointeesError());
        Member member=iUserRepo.getMember(userName);
        member.addPermissionToRole(permission, storeId);
        logger.info("{} got permission to store {} to {}",userName,storeId,permission);

    }
    public void removePremssionFromStore(String removerUsername,String userName, int storeId,Permission permission){
        logger.info("{} remove permission to store {} to {}",userName,storeId,permission);
        if(!iUserRepo.getMember(removerUsername).getRoleOfStore(storeId).getAppointers().contains(userName))
            throw new IllegalStateException(Error.makeUserCanOnlyEditPermissionsToApointeesError());
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
    public void setBirthDate(String userName, LocalDate birthDate) {
        logger.info("set birth date for {}={}", userName,birthDate);
        Member member = iUserRepo.getMember(userName);
        member.setBirthday(birthDate);
        logger.info("done set birth date for {}", userName);
    }
    private void isValid(String detail){
        logger.info("check if field is valid");
        if(detail==null||detail.trim().equals("")){
            throw new IllegalArgumentException(Error.makeValidStringError());
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

    public List<Permission> getMemberPermissionsEnum(String userName, int storeId){
        List<Integer> permissionNums = getMemberPermissions(userName, storeId);
        List<Permission> permissions = new ArrayList<>();
        for(int permission : permissionNums) {
            permissions.add(Permission.getEnumByInt(permission));
        }
        return permissions;
    }

    public List<UserRoleDTO> getMemberRoles(String userName){
        logger.info("get user roles for {}",userName);
        Member member=iUserRepo.getMember(userName);
        List<UserRoleDTO> userRoles=member.getUserRolesString();
        for (UserRoleDTO userRoleDTO : userRoles) {
            String storeName=storeFacade.getStoreInfo(userName,userRoleDTO.getStoreId()).getStoreName();
            userRoleDTO.setStoreName(storeName);
        }
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

     public List<String> getUserOrdersV2(String username){
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


    private double calculateFinalPrice(String username,List<ProductDataPrice> storePriceData){
        logger.info("calculate final price for user {} with items {}",username,storePriceData);
        double sum=0;
        for(ProductDataPrice productDataPrice: storePriceData){
            sum=sum+productDataPrice.getNewPrice();
        }
        logger.info("finished calculate final price for user {} with items {} and got {}",username,storePriceData, sum);
        return sum;
    }
    private double calculateOldPrice(String username,List<ProductDataPrice> storePriceData){
        logger.info("calculate old price for user {} with items {}",username,storePriceData);
        double sum=0;
        for(ProductDataPrice productDataPrice: storePriceData){
                sum=sum+productDataPrice.getOldPrice();
        }
        logger.info("finished calculate old price for user {} with items {} and got {}",username,storePriceData, sum);
        return sum;
    }

    public UserOrderDTO viewCart(String username) throws Exception {
        logger.info("view cart for user {}",username);
        List<CartItemDTO> items=iUserRepo.getUserCart(username);
        List<ProductDataPrice> storePriceData=storeFacade.calculatePrice(username, items);
        logger.info("finished view cart for user {}",username);
        return new UserOrderDTO(storePriceData,calculateOldPrice(username, storePriceData),calculateFinalPrice(username, storePriceData)); 
    }

    public UserOrderDTO viewCart(int guestId) throws Exception {
        logger.info("view cart for guest {}",guestId);
        List<CartItemDTO> items=iUserRepo.getGuestCart(guestId);
        List<ProductDataPrice> storeApriceData=storeFacade.calculatePrice(null, items);
        logger.info("finished view cart for guest {}",guestId);
        return new UserOrderDTO(storeApriceData,calculateOldPrice(null, storeApriceData),calculateFinalPrice(null, storeApriceData));    
    }

    public void checkCart(String username){
        logger.info("check cart for user {}",username);
        List<CartItemDTO> items=iUserRepo.getUserCart(username);
        storeFacade.checkCart(username, items);
        logger.info("finished check cart for user {} without errors",username);
    }
    public void checkCart(int guestId){
        logger.info("check cart for guest {}",guestId);
        List<CartItemDTO> items=iUserRepo.getGuestCart(guestId);
        storeFacade.checkCart(null, items);
        logger.info("finished check cart for guest {} without errors",guestId);
    }

    public void purchaseCart(String username,CreditCardDTO creditCard,AddressDTO addressDTO) throws Exception {
        logger.info("purchase cart for user {} with credit card {} and address {}",username,creditCard,addressDTO);
        List<CartItemDTO> items=iUserRepo.getUserCart(username);
        validateCreditCard(creditCard);
        validateAddress(addressDTO);
        storeFacade.checkCart(null, items);
        List<ProductDataPrice> productList=storeFacade.calculatePrice(username, items);
        Map<Integer,Integer> productAmount=new HashMap<>();
        String supplyString = makeSuplyment(productAmount,addressDTO);
        createUserOrders(productList,creditCard,supplyString,username);
        storeFacade.buyCart(username, items);
        logger.info("finish purchase cart for user {} with credit card {} and address {}",username,creditCard,addressDTO);
    }

    public void purchaseCart(int guestId,CreditCardDTO creditCard,AddressDTO addressDTO) throws Exception {
        logger.info("purchase cart for guest {} with credit card {} and address {}",guestId,creditCard,addressDTO);
        List<CartItemDTO> items=iUserRepo.getGuestCart(guestId);
        validateCreditCard(creditCard);
        validateAddress(addressDTO);
        storeFacade.checkCart(null, items);
        List<ProductDataPrice> productList=storeFacade.calculatePrice(null, items);
        Map<Integer,Integer> productAmount=new HashMap<>();
        String supplyString = makeSuplyment(productAmount,addressDTO);
        createUserOrders(productList,creditCard,supplyString,null);
        storeFacade.buyCart(null, items);
        logger.info("finish purchase cart for guest {} with credit card {} and address {}",guestId,creditCard,addressDTO);
    }
    private void validateAddress(AddressDTO address){
        logger.info("validate address {}",address);
        if(address.getCountry().isEmpty() || address.getCity().isEmpty() || address.getAddressLine1().isEmpty()){
            throw new IllegalArgumentException(Error.makePurchaseMissingAddressError());
        }
        logger.info("address is valid {}",address);
    }
    private void validateCreditCard(CreditCardDTO creditCard){
        logger.info("validate credit card {}",creditCard);
        if(creditCard.getDigitsOnTheBack().isEmpty() || creditCard.getCreditCardNumber().isEmpty() || creditCard.getOwnerId().isEmpty()){
            throw new IllegalArgumentException(Error.makePurchaseMissingCardError());
        }
        PaymentService payment = PaymentService.getInstance();
        if(!payment.checkCardValid(creditCard)){
            throw new IllegalArgumentException(Error.makePurchaseInvalidCardError());
        }
        logger.info("credit card is valid {}",creditCard);
    }
    private void createUserOrders(List<ProductDataPrice> storeBag, CreditCardDTO creditCard, String supplyString,String username){
        logger.info("create user orders");
        SupplyService supply=SupplyService.getInstance();
        PaymentService payment = PaymentService.getInstance();
        Map<Integer,Double> payAmount = getStorePrice(storeBag);
        for(int storeId : payAmount.keySet()){       
            if(!payment.pay(payAmount.get(storeId), creditCard, storeFacade.getStoreBankAccount(storeId))){
                supply.cancelOrder(supplyString);
                throw new IllegalStateException(Error.makePurchasePaymentCannotBeCompletedForStoreError(storeId));
            }
            Map<Integer,List<ProductDataPrice>> productAmounts=new HashMap<>();
            List<ProductDataPrice> productList=getStoreOrder(storeBag, storeId);
            productAmounts.put(storeId, productList);      
            int orderId=orderFacade.createOrder(productAmounts,username);
            if(username!=null)
                iUserRepo.getMember(username).addOrder(orderId);
        }
        logger.info("done create user orders");
    }
    public List<OrderDTO> getAllOrders(String username){
        logger.info("get all orders for {}",username);
        if(!isExist(username))
            throw new NoSuchElementException(Error.makeUserDoesntExistError());
        if(!iUserRepo.getMember(username).isLoggedIn())
            throw new IllegalStateException(Error.makeUserLoggedInError());
        if(!username.equals(systemManagerUserName))
            throw new IllegalStateException(Error.makeSystemManagerCanOnlyViewOrdersError());
        logger.info("got all orders for {}",username);
        return orderFacade.getAllOrders();
    }
    private String makeSuplyment(Map<Integer,Integer> productAmount,AddressDTO addressDTO){
        logger.info("make supplyment {} with address {}",productAmount,addressDTO);
        SupplyService supply=SupplyService.getInstance();
        OrderDetailsDTO orderDetailsDTO=new OrderDetailsDTO(productAmount);
        if(!supply.canMakeOrder(orderDetailsDTO, addressDTO)){
            throw new IllegalStateException(Error.makePurchaseOrderCannotBeSuppliedError());
        }
        String supplyString = supply.makeOrder(orderDetailsDTO, addressDTO);
        logger.info("done make supplyment {}",supplyString);
        return supplyString;
    }
    private Map<Integer,Double> getStorePrice(List<ProductDataPrice> storeBag){
        logger.info("get store price");
        Map<Integer,Double> productAmounts=new HashMap<>();
        for(ProductDataPrice productDataPrice: storeBag){
            if(productAmounts.containsKey(productDataPrice.getStoreId())){
                productAmounts.put(productDataPrice.getStoreId(), productAmounts.get(productDataPrice.getStoreId())+productDataPrice.getNewPrice());
            }else{
                productAmounts.put(productDataPrice.getStoreId(), productDataPrice.getNewPrice());
            }
        }
        logger.info("finish get store price");
        return productAmounts;
    }
    private List<ProductDataPrice> getStoreOrder(List<ProductDataPrice> allOrder,int store_id){
        List<ProductDataPrice> storeOrder=new ArrayList<>();
        for(ProductDataPrice productDataPrice: allOrder){
            if(productDataPrice.getStoreId()==store_id){
                storeOrder.add(productDataPrice);
            }
        }
        return storeOrder;

    }
    //only for tests
    public List<CartItemDTO> getCartItems(int guest_id){
        logger.info("get guest cart");
        return iUserRepo.getGuestCart(guest_id);
    }

}
