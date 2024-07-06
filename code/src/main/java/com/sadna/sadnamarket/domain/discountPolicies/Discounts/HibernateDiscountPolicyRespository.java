package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.domain.buyPolicies.BuyPolicyDTO;
import com.sadna.sadnamarket.domain.discountPolicies.Conditions.Condition;
import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.domain.stores.StoreInfo;
import com.sadna.sadnamarket.service.Error;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HibernateDiscountPolicyRespository implements IDiscountPolicyRepository{

    @Override
    public boolean discountPolicyExists(int policyId) {
        try {
            findDiscountPolicyByID(policyId);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<Integer> getAllPolicyIds() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> res = session.createQuery( "select d.id from discounts d" ).list();
            return new HashSet<>(res);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    public Discount findDiscountPolicyByID(int policyId) throws Exception {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Discount discount = session.get(Discount.class, policyId);
            if (discount == null) {
                throw new IllegalArgumentException(Error.makeNoDiscountWithIdExistError(policyId));
            }
            return discount;
        }
    }

    @Override
    public int addMaximumDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        Discount newDiscountPolicy = new MaximumDiscount(discountA, discountB);
        return addDiscountPolicy(newDiscountPolicy);
    }

    @Override
    public int addOrDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        Discount newDiscountPolicy = new OrDiscount(discountA, discountB);
        return addDiscountPolicy(newDiscountPolicy);
    }

    @Override
    public int addOnCategorySimpleDiscount(double percentage, String categoryName, Condition condition) throws JsonProcessingException {
        if(percentage >100 || percentage <0){
            throw new IllegalArgumentException(Error.percentageForDiscountIsNotInRange(percentage));
        }
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(percentage, condition);
        newDiscountPolicy.setOnCategoryName(categoryName);
        return addDiscountPolicy(newDiscountPolicy);
    }

    @Override
    public int addOnProductSimpleDiscount(double percentage, int productID, Condition condition) throws JsonProcessingException {
        if(percentage >100 || percentage <0){
            throw new IllegalArgumentException(Error.percentageForDiscountIsNotInRange(percentage));
        }
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(percentage, condition);
        newDiscountPolicy.setOnProductID(productID);
        return addDiscountPolicy(newDiscountPolicy);
    }

    @Override
    public int addOnStoreSimpleDiscount(double percentage, Condition condition) throws JsonProcessingException {
        if(percentage >100 || percentage <0){
            throw new IllegalArgumentException(Error.percentageForDiscountIsNotInRange(percentage));
        }
        SimpleDiscount newDiscountPolicy = new SimpleDiscount(percentage, condition);
        newDiscountPolicy.setOnStore();
        return addDiscountPolicy(newDiscountPolicy);
    }

    @Override
    public int addTakeMaxXorDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        XorDiscount newDiscountPolicy = new XorDiscount(discountA, discountB);
        newDiscountPolicy.setMax();
        return addDiscountPolicy(newDiscountPolicy);
    }

    @Override
    public int addTakeMinXorDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        XorDiscount newDiscountPolicy = new XorDiscount(discountA, discountB);
        newDiscountPolicy.setMin();
        return addDiscountPolicy(newDiscountPolicy);
    }

    @Override
    public int addAdditionDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        Discount newDiscountPolicy = new AdditionDiscount(discountA, discountB);
        return addDiscountPolicy(newDiscountPolicy);
    }

    @Override
    public int addAndDiscount(Discount discountA, Discount discountB) throws JsonProcessingException {
        Discount newDiscountPolicy = new AndDiscount(discountA, discountB);
        return addDiscountPolicy(newDiscountPolicy);
    }

    private int addDiscountPolicy(Discount discount){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(discount);
            transaction.commit();
            return discount.getId();
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }
}
