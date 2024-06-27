package com.sadna.sadnamarket.domain.stores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.service.Error;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.transaction.Transactional;

public class HibernateStoreRepository implements IStoreRepository{

    @Override
    public Store findStoreByID(int storeId) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            return store;
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

    @Override
    public int addStore(String founderUsername, String storeName, String address, String email, String phoneNumber) {
        if (storeNameExists(storeName))
            throw new IllegalArgumentException(
                    Error.makeStoreWithNameAlreadyExistsError(storeName));

        //Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Store createdStore = new Store(founderUsername, new StoreInfo(storeName, address, email, phoneNumber));
            session.save(createdStore); // Save the store and get the generated ID
            transaction.commit();
            return createdStore.getStoreId();
        }
        catch (Exception e) {
            transaction.rollback();
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
        try {
            session.beginTransaction();
            session.merge(store);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            session.getTransaction().rollback();
        }
        finally {
            session.close();
        }
    }

    @Override
    @Transactional
    public void addProductToStore(int storeId, int productId, int amount) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            if (store.getProductAmounts().containsKey(productId)) {
                throw new IllegalArgumentException(Error.makeStoreProductAlreadyExistsError(productId));
            }
            store.addProduct(productId, amount);
            session.merge(store);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            session.getTransaction().rollback();
        }
        finally {
            session.close();
        }
    }

    @Override
    public boolean productExists(int storeId, int productId) {
        return findStoreByID(storeId).getProductAmounts().containsKey(productId);
    }

    @Override
    public boolean hasProductInStock(int storeId, int productId, int amount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            if (!store.getProductAmounts().containsKey(productId)) {
                throw new IllegalArgumentException(Error.makeProductDoesntExistError(productId));
            }
            return store.hasProductInAmount(productId, amount);
        }
    }

    @Override
    public int getProductAmountInStore(int storeId, int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            if (!store.getProductAmounts().containsKey(productId)) {
                throw new IllegalArgumentException(Error.makeProductDoesntExistError(productId));
            }
            return store.getProductAmount(productId);
        }
    }

    @Override
    public void deleteProductFromStore(int storeId, int productId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            if (!store.getProductAmounts().containsKey(productId)) {
                throw new IllegalArgumentException(Error.makeProductDoesntExistError(productId));
            }
            store.deleteProduct(productId);
            String deleteQuery = "DELETE FROM store_products WHERE store_id = :storeId AND product_id = :productId";
            Query query = session.createNativeQuery(deleteQuery).setParameter("storeId", storeId).setParameter("productId", productId);
            query.executeUpdate();
            session.getTransaction().commit();
        }
        catch (Exception e) {
            session.getTransaction().rollback();
        }
        finally {
            session.close();
        }
    }

    @Override
    public void updateProductAmountInStore(int storeId, int productId, int newAmount) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            if (!store.getProductAmounts().containsKey(productId)) {
                throw new IllegalArgumentException(Error.makeProductDoesntExistError(productId));
            }
            store.setProductAmounts(productId, newAmount);
            session.update(store);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            session.getTransaction().rollback();
        }
        finally {
            session.close();
        }
    }

    @Override
    public StoreDTO getStoreDTO(int storeId) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            StoreDTO dto = store.getStoreDTO();
            dto.setProductAmounts(new HashMap<>(store.getProductAmounts())); // for lazy loading
            return dto;
        }
    }

    @Override
    public Set<String> checkCartInStore(int storeId, List<CartItemDTO> cart) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            return store.checkCart(cart);
        }
    }

    @Override
    public Set<String> updateStockInStore(int storeId, List<CartItemDTO> cart) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            Set<String> errors = store.updateStock(cart);
            saveStore(store);
            return errors;
        }
    }

    @Override
    public Store findStoreByName(String storeName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findStoreByName'");
    }
}
