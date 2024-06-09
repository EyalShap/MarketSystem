package com.sadna.sadnamarket.domain.discountPolicies;

import com.sadna.sadnamarket.domain.discountPolicies.Discounts.Discount;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountPolicyFacade {

    private Map<Integer, DiscountPolicyManager> mapper;
    private ProductFacade productFacade;
    private static DiscountPolicyFacade instance;

    public DiscountPolicyFacade(ProductFacade productFacade) {
        mapper = new HashMap<Integer, DiscountPolicyManager>();
        this.productFacade = productFacade;
    }

    public static DiscountPolicyFacade getInstance() {
        if (instance == null) {
            instance = new DiscountPolicyFacade(new ProductFacade());
        }
        return instance;
    }

    // for now that function doesn't do anything special
    public void addDiscountPolicyToStore(int storeId, int policyId) throws Exception {
        if(!discountPolicyRepository.buyPolicyExists(policyId))
            throw new Exception();
        if(!mapper.containsKey(storeId))
            mapper.put(storeId, new DiscountPolicyManager(this));
        DiscountPolicyManager manager = mapper.get(storeId);
        manager.addDiscountPolicy(policyId);
    }

    public List<ProductDataPrice> calculatePrice(int storeId, List<CartItemDTO> cart) {
        DiscountPolicyManager discountManager = mapper.get(storeId);
        Map<Integer, ProductDTO> productDTOMap = new HashMap<>();
        for(CartItemDTO cartItemDTO : cart){
            productDTOMap.put(cartItemDTO.getProductId(), productFacade.getProductDTO(cartItemDTO.getProductId()));
        }
        return discountManager.giveDiscount(cart, productDTOMap);
    }

    public Discount getDiscount(Integer discountID) {
    }
}
