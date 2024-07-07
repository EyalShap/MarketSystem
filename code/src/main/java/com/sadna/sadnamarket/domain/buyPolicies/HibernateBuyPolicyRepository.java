package com.sadna.sadnamarket.domain.buyPolicies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.service.Error;
import jakarta.persistence.QueryHint;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HibernateBuyPolicyRepository implements IBuyPolicyRepository{
    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public BuyPolicy findBuyPolicyByID(int policyId) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            BuyPolicyData policyDTO = session.get(BuyPolicyData.class, policyId);
            if (policyDTO == null) {
                throw new IllegalArgumentException(Error.makeBuyPolicyWithIdDoesNotExistError(policyId));
            }
            if(policyDTO.isComposite()){
                BuyPolicy policy1 = findBuyPolicyByID(policyDTO.getId1());
                BuyPolicy policy2 = findBuyPolicyByID(policyDTO.getId2());
                return policyDTO.toBuyPolicy(policy1, policy2);
            }else{
                return policyDTO.toBuyPolicy();
            }
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public Set<Integer> getAllPolicyIds() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> res = session.createQuery( "select p.policyId from BuyPolicyData p" ).list();
            return new HashSet<>(res);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    private int addBuyPolicy(BuyPolicy buyPolicy) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            BuyPolicyData hibernateDto = buyPolicy.generateData();
            BuyPolicyData existingDto = getExistingBuyPolicy(session, hibernateDto);
            if(existingDto != null){
                transaction.commit();
                return existingDto.policyId;
            }
            session.save(hibernateDto); // Save the store and get the generated ID
            transaction.commit();
            return hibernateDto.policyId;
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    public int addProductKgBuyPolicy(int productId, List<BuyType> buytypes, double min, double max) throws JsonProcessingException {
        KgLimitBuyPolicy policy = new KgLimitBuyPolicy(buytypes,new ProductSubject(productId),min,max);
        return addBuyPolicy(policy);
    }

    @Override
    public int addProductAmountBuyPolicy(int productId, List<BuyType> buytypes, int min, int max) throws JsonProcessingException {
        AmountBuyPolicy policy = new AmountBuyPolicy(buytypes,new ProductSubject(productId),min,max);
        return addBuyPolicy(policy);
    }

    @Override
    public int addCategoryAgeLimitBuyPolicy(String category, List<BuyType> buytypes, int min, int max) throws JsonProcessingException {
        AgeLimitBuyPolicy policy = new AgeLimitBuyPolicy(buytypes,new CategorySubject(category),min,max);
        return addBuyPolicy(policy);
    }

    @Override
    public int addCategoryHourLimitBuyPolicy(String category, List<BuyType> buytypes, LocalTime from, LocalTime to) throws JsonProcessingException {
        HourLimitBuyPolicy policy = new HourLimitBuyPolicy(buytypes,new CategorySubject(category),from,to);
        return addBuyPolicy(policy);
    }

    @Override
    public int addCategoryRoshChodeshBuyPolicy(String category, List<BuyType> buytypes) throws JsonProcessingException {
        RoshChodeshBuyPolicy policy = new RoshChodeshBuyPolicy(buytypes,new CategorySubject(category));
        return addBuyPolicy(policy);
    }

    @Override
    public int addCategoryHolidayBuyPolicy(String category, List<BuyType> buytypes) throws JsonProcessingException {
        HolidayBuyPolicy policy = new HolidayBuyPolicy(buytypes,new CategorySubject(category));
        return addBuyPolicy(policy);
    }

    @Override
    public int addCategorySpecificDateBuyPolicy(String category, List<BuyType> buytypes, int day, int month, int year) throws JsonProcessingException {
        SpecificDateBuyPolicy policy = new SpecificDateBuyPolicy(buytypes,new CategorySubject(category),day,month,year);
        return addBuyPolicy(policy);
    }

    @Override
    public int addAndBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        AndBuyPolicy policy = new AndBuyPolicy(policy1, policy2);
        return addBuyPolicy(policy);
    }

    @Override
    public int addOrBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        OrBuyPolicy policy = new OrBuyPolicy(policy1, policy2);
        return addBuyPolicy(policy);
    }

    @Override
    public int addConditioningBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) throws JsonProcessingException {
        ConditioningBuyPolicy policy = new ConditioningBuyPolicy(policy1, policy2);
        return addBuyPolicy(policy);
    }

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    private BuyPolicyData getExistingBuyPolicy(Session session, BuyPolicyData policyDTO){
        Query query = policyDTO.getUniqueQuery(session);
        List<BuyPolicyData> res = query.list();
        if(res.isEmpty()){
            return null;
        }
        return res.get(0);
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public boolean buyPolicyExists(int policyId) {
        try {
            findBuyPolicyByID(policyId);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public BuyPolicyManager createManager(BuyPolicyFacade facade, int storeId) {
        return new HibernateBuyPolicyManager(facade,storeId);
    }

    @Override
    public void clear() {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery( "delete from BuyPolicyData").executeUpdate();
            session.createQuery( "delete from StoreBuyPolicyRelation").executeUpdate();
            transaction.commit();
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }
}
