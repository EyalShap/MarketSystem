package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.discountPolicies.DiscountManager;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicy;
import com.sadna.sadnamarket.domain.discountPolicies.DiscountPolicyFacade;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.domain.users.UserFacade;

import java.time.LocalTime;
import java.util.*;

public class BuyPolicyFacade {
    private Map<Integer, BuyPolicyManager> mapper;
    private ProductFacade productFacade;
    private UserFacade userFacade;
    private int nextId;

    public BuyPolicyFacade() {
        this.mapper = new HashMap<Integer, BuyPolicyManager>();
        this.nextId = 0;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    // for now that function dosent do anything special
    /*public boolean addBuyPolicy(BuyPolicyType policyType, int storeId, String args) {
        BuyPolicy bp;
        synchronized(mapper){
            if (!mapper.containsKey(storeId)) {
                mapper.put(storeId, new BuyPolicyManager());
            }
        }
        try {
            // add new DiscountPolicy
            bp = new DefaultBuyPolicy(new LinkedList<>());
        } catch (Exception e) {
            return false;
        }
        BuyPolicyManager buyPolicyManager = mapper.get(storeId);
        buyPolicyManager.addBuyPolicy(bp);
        return true;

    }*/

    private void addBuyPolicy(int storeId, BuyPolicy buyPolicy) {
        synchronized (mapper) {
            BuyPolicyManager addTo = mapper.getOrDefault(storeId, new BuyPolicyManager());
            addTo.addBuyPolicy(buyPolicy);
        }
    }

    // only the ones that are in the requirements for now, we can add more if needed
    public void addProductKgBuyPolicy(int storeId, int productId, List<BuyType> buytypes, double min, double max) {
        BuyPolicy newPolicy =  new KgLimitBuyPolicy(nextId, buytypes, new ProductSubject(productId), min, max);
        addBuyPolicy(storeId, newPolicy);
        nextId++;
    }

    public void addProductAmountBuyPolicy(int storeId, int productId, List<BuyType> buytypes, int min, int max) throws Exception {
        BuyPolicy newPolicy = new AmountBuyPolicy(buytypes, new ProductSubject(productId), min, max);
        addBuyPolicy(storeId, newPolicy);
        nextId++;
    }

    public void addCategoryAgeLimitBuyPolicy(int storeId, String category, List<BuyType> buytypes, int min, int max) throws Exception {
        BuyPolicy newPolicy = new AgeLimitBuyPolicy(buytypes, new CategorySubject(category), min, max);
        addBuyPolicy(storeId, newPolicy);
        nextId++;
    }

    public void addCategoryHourLimitBuyPolicy(int storeId, String category, List<BuyType> buytypes, LocalTime from, LocalTime to) throws Exception {
        BuyPolicy newPolicy = new HourLimitBuyPolicy(buytypes, new CategorySubject(category), from, to);
        addBuyPolicy(storeId, newPolicy);
        nextId++;
    }

    public void addCategoryRoshChodeshBuyPolicy(int storeId, String category, List<BuyType> buytypes) throws Exception {
        BuyPolicy newPolicy = new RoshChodeshBuyPolicy(nextId, buytypes, new CategorySubject(category));
        addBuyPolicy(storeId, newPolicy);
        nextId++;
    }

    public void addCategoryHolidayBuyPolicy(int storeId, String category, List<BuyType> buytypes) throws Exception {
        BuyPolicy newPolicy = new HolidayBuyPolicy(nextId, buytypes, new CategorySubject(category));
        addBuyPolicy(storeId, newPolicy);
        nextId++;
    }
    // --------------------------------------------------------------------------------
    
    public String canBuy(int storeId, List<CartItemDTO> cart, String username) {
        BuyPolicyManager buyPolicyManager = mapper.get(storeId);
        MemberDTO user = userFacade.isExist(username) ?  userFacade.getMemberDTO(username) : null;
        return buyPolicyManager.canBuy(cart, getProductDTOs(cart), user);
    }

    private Map<Integer, ProductDTO> getProductDTOs(List<CartItemDTO> cart) {
        Map<Integer, ProductDTO> res = new HashMap<>();
        for(CartItemDTO item : cart) {
            int productId = item.getProductId();
            res.put(productId, productFacade.getProductDTO(productId));
        }
        return res;
    }

}
