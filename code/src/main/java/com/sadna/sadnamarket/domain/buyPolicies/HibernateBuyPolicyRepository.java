package com.sadna.sadnamarket.domain.buyPolicies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.domain.stores.Store;
import com.sadna.sadnamarket.domain.stores.StoreInfo;
import com.sadna.sadnamarket.service.Error;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class HibernateBuyPolicyRepository implements IBuyPolicyRepository{
    @Override
    public BuyPolicy findBuyPolicyByID(int policyId) {
        return null;
    }

    @Override
    public Set<Integer> getAllPolicyIds() {
        return null;
    }

    private int addBuyPolicy(BuyPolicy buyPolicy) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(buyPolicy); // Save the store and get the generated ID
            transaction.commit();
            return buyPolicy.getId();
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    public int addProductKgBuyPolicy(int productId, List<BuyType> buytypes, double min, double max) throws JsonProcessingException {
        BuyPolicy newPolicy =  new KgLimitBuyPolicy(buytypes, new ProductSubject(productId), min, max);
        return addBuyPolicy(newPolicy);
    }

    @Override
    public int addProductAmountBuyPolicy(int productId, List<BuyType> buytypes, int min, int max) throws JsonProcessingException {
        BuyPolicy newPolicy = new AmountBuyPolicy(buytypes, new ProductSubject(productId), min, max);
        return addBuyPolicy(newPolicy);
    }

    @Override
    public int addCategoryAgeLimitBuyPolicy(String category, List<BuyType> buytypes, int min, int max) throws JsonProcessingException {
        BuyPolicy newPolicy = new AgeLimitBuyPolicy(buytypes, new CategorySubject(category), min, max);
        return addBuyPolicy(newPolicy);
    }

    @Override
    public int addCategoryHourLimitBuyPolicy(String category, List<BuyType> buytypes, LocalTime from, LocalTime to) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addCategoryRoshChodeshBuyPolicy(String category, List<BuyType> buytypes) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addCategoryHolidayBuyPolicy(String category, List<BuyType> buytypes) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addCategorySpecificDateBuyPolicy(String category, List<BuyType> buytypes, int day, int month, int year) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addAndBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addOrBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        return 0;
    }

    @Override
    public int addConditioningBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        return 0;
    }

    @Override
    public boolean buyPolicyExists(int policyId) {
        return false;
    }
}
