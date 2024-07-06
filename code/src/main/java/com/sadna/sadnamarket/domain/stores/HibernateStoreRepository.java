package com.sadna.sadnamarket.domain.stores;

import java.util.*;

import com.sadna.sadnamarket.HibernateUtil;
import com.sadna.sadnamarket.domain.payment.BankAccountDTO;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.products.ProductFacade;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.service.Error;
import jakarta.persistence.QueryHint;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.NoResultException;
import org.springframework.data.jpa.repository.QueryHints;
import javax.transaction.Transactional;

public class HibernateStoreRepository implements IStoreRepository{

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
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
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public Set<Integer> getAllStoreIds() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> res = session.createQuery( "select s.storeId from Store s" ).list();
            return new HashSet<>(res);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    private void verify(String storeName, String address, String email, String phoneNumber) {
        if(storeName == null || storeName.trim().equals(""))
            throw new IllegalArgumentException(Error.makeStoreNotValidAspectError(storeName, "store name"));
        if(address == null || address.trim().equals(""))
            throw new IllegalArgumentException(Error.makeStoreNotValidAspectError(address, "address"));
        if(email == null || !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
            throw new IllegalArgumentException(Error.makeStoreNotValidAspectError(email, "email address"));
        if(phoneNumber == null || phoneNumber.matches("^\\d{9}$"))
            throw new IllegalArgumentException(Error.makeStoreNotValidAspectError(phoneNumber, "phone number"));
    }

    @Override
    public int addStore(String founderUsername, String storeName, String address, String email, String phoneNumber) {
        verify(storeName, address, email, phoneNumber);
        if (storeNameExists(storeName))
            throw new IllegalArgumentException(
                    Error.makeStoreWithNameAlreadyExistsError(storeName));

        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Store createdStore = new Store(founderUsername, new StoreInfo(storeName, address, email, phoneNumber));
            session.save(createdStore);
            //StoreDTO newStore = new StoreDTO(storeName, address, email, phoneNumber, founderUsername);
            //session.save(newStore);
            transaction.commit();
            return createdStore.getStoreId();
        }
        catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    private boolean storeNameExists(String storeName) {
        try {
            findStoreByName(storeName);
            return true;
        }
        catch (Exception e) {
            return false;
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
        int amountInStore;
        try {
            amountInStore = getProductAmountInStore(storeId, productId);
            return amountInStore >= amount;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public int getProductAmountInStore(int storeId, int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            if (!store.getProductAmounts().containsKey(productId)) {
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId, productId));
            }
            return store.getProductAmounts().get(productId);
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
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId, productId));
            }
            store.deleteProduct(productId);
            String deleteQuery = "DELETE FROM store_products WHERE store_id = :storeId AND product_id = :productId";
            Query query = session.createNativeQuery(deleteQuery).setParameter("storeId", storeId).setParameter("productId", productId);
            query.executeUpdate();
            session.getTransaction().commit();
        }
        catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
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
                throw new IllegalArgumentException(Error.makeStoreProductDoesntExistError(storeId, productId));
            }

            store.setProductAmounts(productId, newAmount);
            session.update(store);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        finally {
            session.close();
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public StoreDTO getStoreDTO(int storeId) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            // for lazy loading
            //store.getProductAmounts();
            for(int productId : store.getProductAmounts().keySet()) {
                store.getProductAmounts().get(productId);
            }
            return store.getStoreDTO();
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
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
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public Store findStoreByName(String storeName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Store res = session.createQuery("SELECT s FROM Store s where s.storeInfo.storeName = :name", Store.class).setParameter("name", storeName).getSingleResult();
            return res;
        }
        catch (NoResultException e) {
            throw new IllegalArgumentException(Error.makeNoStoreWithNameError(storeName));
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    public void setStoreBankAccount(int storeId, BankAccountDTO bankAccountDTO) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            store.setBankAccount(bankAccountDTO);
            session.merge(store);
            session.save(bankAccountDTO);
            transaction.commit();
        }
        catch (Exception e) {
            transaction.rollback();
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public BankAccountDTO getStoreBankAccount(int storeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            //StoreDTO res = session.createQuery("select s from StoreDTO s where s.storeName = :name", StoreDTO.class).getSingleResult();
            //return new Store(res);
            BankAccountDTO bankAccountDTO = session.get(BankAccountDTO.class, storeId);
            return bankAccountDTO;
        }
        catch (NoResultException e) {
            throw new IllegalArgumentException(Error.makeStoreDidNotSetBankAccountError(storeId));
        }
        catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    /*@Override
    public boolean areStoresEqual(Store s1, Store s2) {
        if(!s1.equals(s2))
            return false;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> res1 = session.createQuery("SELECT DISTINCT key(sp) FROM StoreDTO s JOIN s.productAmounts sp WHERE s.storeId = :storeId").setParameter("storeId", s1.getStoreId()).list();
            List<Integer> res2 = session.createQuery("SELECT DISTINCT key(sp) FROM StoreDTO s JOIN s.productAmounts sp WHERE s.storeId = :storeId").setParameter("storeId", s2.getStoreId()).list();
            return Objects.equals(new HashSet<>(res1), new HashSet<>(res2));
        }
    }*/

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public boolean areProductsInStore(int storeId, Set<Integer> productIds) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            return store.hasProducts(productIds);
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public Map<ProductDTO, Integer> getProductsInfoAndFilter(ProductFacade productFacade, int storeId, String productName, String category, double price, double minProductRank) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Store store = session.get(Store.class, storeId);
            if (store == null) {
                throw new IllegalArgumentException(Error.makeStoreNoStoreWithIdError(storeId));
            }
            Map<Integer, Integer> productAmounts = store.getProductAmounts();
            List<Integer> storeProductIds = new ArrayList<>(productAmounts.keySet());
            List<ProductDTO> filteredProducts = productFacade.getFilteredProducts(storeProductIds, productName, price, category, minProductRank);
            Map<ProductDTO, Integer> res = new HashMap<>();
            for (ProductDTO product : filteredProducts)
                res.put(product, productAmounts.get(product.getProductID()));
            return res;
        }
    }

}
