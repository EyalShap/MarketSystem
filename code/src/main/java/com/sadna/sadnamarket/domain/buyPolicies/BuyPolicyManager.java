package com.sadna.sadnamarket.domain.buyPolicies;

import java.util.*;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

public class BuyPolicyManager {
    private List<Integer> buyPolicyIds;
    private List<Integer> lawsBuyPolicyIds;
    private BuyPolicyFacade facade;

    public BuyPolicyManager(BuyPolicyFacade facade) {
        this.buyPolicyIds = new ArrayList<>();
        this.lawsBuyPolicyIds = new ArrayList<>();
        this.facade = facade;
    }

    public boolean hasPolicy(int policyId) {
        synchronized (buyPolicyIds) {
            synchronized (lawsBuyPolicyIds) {
                return buyPolicyIds.contains(policyId) || lawsBuyPolicyIds.contains(policyId);
            }
        }
    }


    public void addBuyPolicy(int buyPolicyId) {
        synchronized (buyPolicyIds) {
            synchronized (lawsBuyPolicyIds) {
                if (hasPolicy(buyPolicyId))
                    throw new IllegalArgumentException(Error.makeBuyPolicyAlreadyExistsError(buyPolicyId));
                buyPolicyIds.add(buyPolicyId);
            }
        }
    }

    public void addLawBuyPolicy(int buyPolicyId) {
        synchronized (buyPolicyIds) {
            synchronized (lawsBuyPolicyIds) {
                if (hasPolicy(buyPolicyId))
                    throw new IllegalArgumentException(Error.makeBuyPolicyAlreadyExistsError(buyPolicyId));
                lawsBuyPolicyIds.add(buyPolicyId);
            }
        }
    }

    public void removeBuyPolicy(int buyPolicyId) {
        synchronized (buyPolicyIds) {
            synchronized (lawsBuyPolicyIds) {
                if (lawsBuyPolicyIds.contains(buyPolicyId)) {
                    throw new IllegalArgumentException(Error.makeCanNotRemoveLawBuyPolicyError(buyPolicyId));
                }
                if (!hasPolicy(buyPolicyId)) {
                    throw new IllegalArgumentException(Error.makeBuyPolicyWithIdDoesNotExistError(buyPolicyId));
                }
                buyPolicyIds.removeIf(id -> id == buyPolicyId);
            }
        }
    }

    public Set<String> canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        Set<String> error = new HashSet<>();
        // if one policy says that you cant buy return false;
        synchronized (buyPolicyIds) {
            for (Integer policyId : buyPolicyIds) {
                BuyPolicy policy = facade.getBuyPolicy(policyId);
                if (!policy.canBuy(cart, products, user)) {
                    error.add(policy.getErrorDescription());
                }
            }
        }
        synchronized (lawsBuyPolicyIds) {
            for (Integer policyId : lawsBuyPolicyIds) {
                BuyPolicy policy = facade.getBuyPolicy(policyId);
                if (!policy.canBuy(cart, products, user)) {
                    error.add(policy.getErrorDescription());
                }
            }
        }
        return error;
    }
}
