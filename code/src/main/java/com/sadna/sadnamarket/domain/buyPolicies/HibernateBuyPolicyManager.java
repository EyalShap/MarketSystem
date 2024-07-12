package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.HibernateTimeoutLengths;
import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;
import jakarta.persistence.QueryHint;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.QueryHints;

import java.io.IOError;
import java.util.*;

public class HibernateBuyPolicyManager extends BuyPolicyManager{
    private int storeId;
    private Timer timeoutTimer;
    private TimerTask handleTimeout;

    public HibernateBuyPolicyManager(BuyPolicyFacade facade, int storeId) {
        super(facade);
        this.storeId = storeId;
    }

    private void refreshTimerTask(){
        timeoutTimer = new Timer("timeout");
        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println(e);
                System.exit(1);
            }
        };
        handleTimeout = new TimerTask() {
            @Override
            public void run() {
                Thread.currentThread().setUncaughtExceptionHandler(handler);
                throw new InternalError(new RuntimeException("DB Timed Out"));
            }
        };
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public boolean hasPolicy(int policyId) {
        refreshTimerTask();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            timeoutTimer.schedule(handleTimeout, HibernateTimeoutLengths.MS_DB_SHORT+HibernateTimeoutLengths.MS_TIMEOUT_PADDING);
            List<StoreBuyPolicyRelation> list = session.createQuery( "select p.policyId from StoreBuyPolicyRelation p " +
                    "WHERE p.storeId = :storeId " +
                    "AND p.policyId = :policyId" )
                    .setParameter("storeId",storeId)
                    .setParameter("policyId",policyId).list();
            timeoutTimer.cancel();
            return !list.isEmpty();
        }
        catch (Exception e) {
            timeoutTimer.cancel();
            return false;
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<Integer> getAllPolicyIds() {
        refreshTimerTask();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            timeoutTimer.schedule(handleTimeout, HibernateTimeoutLengths.MS_DB_CLEAR_SELECTALL);
            List<Integer> res = session.createQuery( "select p.policyId from StoreBuyPolicyRelation p WHERE p.storeId = :storeId" ).setParameter("storeId",storeId).list();
            timeoutTimer.cancel();
            return res;
        }
        catch (Exception e) {
            timeoutTimer.cancel();
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    public void addBuyPolicy(int buyPolicyId) {
        if (hasPolicy(buyPolicyId))
            throw new IllegalArgumentException(Error.makeBuyPolicyAlreadyExistsError(buyPolicyId));
        refreshTimerTask();
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoreBuyPolicyRelation dto = new StoreBuyPolicyRelation(storeId, buyPolicyId, false);
            session.save(dto); // Save the store and get the generated ID
            timeoutTimer.schedule(handleTimeout, HibernateTimeoutLengths.MS_DB_MEDIUM_SHORT+HibernateTimeoutLengths.MS_TIMEOUT_PADDING);
            transaction.commit();
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
        timeoutTimer.cancel();
    }

    @Override
    public void addLawBuyPolicy(int buyPolicyId) {
        if (hasPolicy(buyPolicyId))
            throw new IllegalArgumentException(Error.makeBuyPolicyAlreadyExistsError(buyPolicyId));
        refreshTimerTask();
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StoreBuyPolicyRelation dto = new StoreBuyPolicyRelation(storeId, buyPolicyId, true);
            session.save(dto); // Save the store and get the generated ID
            timeoutTimer.schedule(handleTimeout, HibernateTimeoutLengths.MS_DB_MEDIUM_SHORT+HibernateTimeoutLengths.MS_TIMEOUT_PADDING);
            transaction.commit();
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
        timeoutTimer.cancel();
    }

    @Override
    public void removeBuyPolicy(int buyPolicyId) {
        if (!hasPolicy(buyPolicyId)) {
            throw new IllegalArgumentException(Error.makeBuyPolicyWithIdDoesNotExistError(buyPolicyId));
        }
        refreshTimerTask();
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<StoreBuyPolicyRelation> list = session.createQuery( "select p from StoreBuyPolicyRelation p " +
                            "WHERE p.storeId = :storeId " +
                            "AND p.policyId = :policyId" )
                    .setParameter("storeId",storeId)
                    .setParameter("policyId",buyPolicyId).list();
            if(list.get(0).legal){
                throw new IllegalArgumentException(Error.makeCanNotRemoveLawBuyPolicyError(buyPolicyId));
            }
            session.delete(list.get(0));
            timeoutTimer.schedule(handleTimeout, HibernateTimeoutLengths.MS_DB_MEDIUM_SHORT+HibernateTimeoutLengths.MS_TIMEOUT_PADDING);
            transaction.commit();
        }
        catch (HibernateException e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
        timeoutTimer.cancel();
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public Set<String> canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        Set<String> error = new HashSet<>();
        List<Integer> buyPolicyIds = getAllPolicyIds();
        for (Integer policyId : buyPolicyIds) {
            BuyPolicy policy = facade.getBuyPolicy(policyId);
            error.addAll(policy.canBuy(cart, products, user));
        }
        return error;
    }

    @Override
    public void clear() {
        Transaction transaction = null;
        refreshTimerTask();
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery( "delete from StoreBuyPolicyRelation WHERE store = :storeId ")
                    .setParameter("storeId",storeId).executeUpdate();
            timeoutTimer.schedule(handleTimeout, HibernateTimeoutLengths.MS_DB_CLEAR_SELECTALL);
            transaction.commit();
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
        timeoutTimer.cancel();
    }
}
