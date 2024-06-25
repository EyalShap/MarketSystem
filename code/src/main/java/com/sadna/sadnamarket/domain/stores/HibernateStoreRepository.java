package com.sadna.sadnamarket.domain.stores;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.service.Error;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.transaction.Transactional;

public class HibernateStoreRepository implements IStoreRepository{

    @Override
    public Store findStoreByID(int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
             Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            return store;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
        }
    }

    @Override
    public Set<Integer> getAllStoreIds() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> res = session.createQuery( "select s.storeId from Store s" ).list();
            return new HashSet<>(res);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    /*@Override
    public void deleteStore(int storeId) {

    }*/

    @Override
    public int addStore(String founderUsername, String storeName, String address, String email, String phoneNumber) {
        if (storeNameExists(storeName))
            throw new IllegalArgumentException(
                    Error.makeStoreWithNameAlreadyExistsError(storeName));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Store createdStore = new Store(founderUsername, new StoreInfo(storeName, address, email, phoneNumber));
            session.save(createdStore); // Save the store and get the generated ID
            session.getTransaction().commit();
            return createdStore.getStoreId();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    private boolean storeNameExists(String storeName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Store> res = session.createQuery( "select s from Store s where s.storeInfo.storeName=:name" ).setParameter( "name", storeName).list();
            return res != null && res.size() > 0;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    public boolean storeExists(int storeId) {
        try {
            findStoreByID(storeId);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public void saveStore(Store store) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.merge(store);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void addProductToStore(int storeId, int productId, int amount) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Store store = session.get(Store.class, storeId);
        if(store == null) {
            throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
        }
        if(store.getProductAmounts().containsKey(productId)) {
            throw new IllegalArgumentException(Error.makeStoreProductAlreadyExistsError(productId));
        }
        store.addProduct(productId, amount);
        session.merge(store);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public boolean productExists(int storeId, int productId) {
        return findStoreByID(storeId).getProductAmounts().containsKey(productId);
    }
}
