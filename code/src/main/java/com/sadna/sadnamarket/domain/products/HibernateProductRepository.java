package com.sadna.sadnamarket.domain.products;

import com.sadna.sadnamarket.HibernateUtil;
import jakarta.persistence.QueryHint;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sadna.sadnamarket.service.Error;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HibernateProductRepository implements IProductRepository{
    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public Product getProduct(int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            ProductDTO productDTO=session.get(ProductDTO.class, productId);
//            return productDTOToProduct(productDTO);
            return session.get(Product.class, productId);
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public Set<Integer> getAllProductIds() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> productIds = session.createQuery("SELECT p.productId FROM Product p", Integer.class).list();
            return new HashSet<>(productIds);
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<Product> getAllProducts() {
        List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            List<ProductDTO>productDTOS= session.createQuery("FROM ProductDTO", ProductDTO.class).list();
//            for (ProductDTO productDTO : productDTOS) {
//                products.add(productDTOToProduct(productDTO));
//            }
//            return products;

            return session.createQuery("FROM Product", Product.class).list();
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<Product> getProducts(List<Integer> productIds) {
        List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery(
                    "FROM Product p WHERE p.productId IN (:productIds)", Product.class);
//            query.setParameterList("productIds", productIds);
//            List<ProductDTO> productDTOs = query.list();
//            for (ProductDTO productDTO : productDTOs) {
//                products.add(productDTOToProduct(productDTO));
//            }
            return query.setParameterList("productIds", productIds).list();//test//
//            return products;
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    public void removeProduct(int productId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, productId);
            if (product != null) {
                session.delete(product);
                transaction.commit();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }
    @Override
    public int addProduct(String productName, double productPrice, String productCategory, double productRank, double productWeight, int storeId,String description) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Product product=new Product();

            product.setProductName(productName);
            product.setProductPrice(productPrice);
            product.setProductCategory(productCategory);
            product.setProductRank(productRank);
            product.setProductWeight(productWeight);
            product.setStoreId(storeId);
            product.setDescription(description);
            product.setActive(true);

            session.save(product);
            transaction.commit();
            return product.getProductId();
        }catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public boolean isExistProduct(int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(p) FROM Product p WHERE p.productId = :productId", Long.class);
            query.setParameter("productId", productId);

            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<Product> filterByName(String productName) {
        //List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Product p WHERE p.productName LIKE :productName";
//            List<ProductDTO> productDTOS=new ArrayList<>();
//            productDTOS= session.createQuery(hql, ProductDTO.class)
//                    .setParameter("productName", "%" + productName + "%")
//                    .list();
//            for (ProductDTO productDTO:productDTOS) {
//                products.add(productDTOToProduct(productDTO));
//            }
//            return products;
            return session.createQuery(hql, Product.class)
                    .setParameter("productName", "%" + productName + "%")
                    .list();
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<Product> filterByCategory(String category) {
        //List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Product p WHERE p.productCategory = :category";
//            List<ProductDTO> productDTOS=new ArrayList<>();
//            productDTOS= session.createQuery(hql, ProductDTO.class)
//                    .setParameter("category", category)
//                    .list();
//            for (ProductDTO productDTO:productDTOS) {
//                products.add(productDTOToProduct(productDTO));
//            }
            //return products;
            return session.createQuery(hql, Product.class)
                    .setParameter("category", category)
                    .list();
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    public List<Product> getTopProducts() {
        List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Product p ORDER BY p.productRank DESC";
//            List<ProductDTO> productDTOS= session.createQuery(hql, ProductDTO.class).list();
//            for (ProductDTO productDTO:productDTOS) {
//                products.add(productDTOToProduct(productDTO));
//            }
//            return products;
            return session.createQuery(hql, Product.class).list();
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    public void clean(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Product").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new IllegalArgumentException("Database error", e);
        }
    }

//    public Product productDTOToProduct(ProductDTO productDTO){
//        int id=productDTO.getProductID();
//        String productName=productDTO.getProductName();
//        double productPrice = productDTO.getProductPrice();
//        String productCategory=productDTO.getProductCategory();
//        double productRank=productDTO.getProductRank();
//        double productWeight=productDTO.getProductWeight();
//        int storeId=productDTO.getStoreId();
//        Product product=new Product(id,productName,productPrice,productCategory,productRank,productWeight,storeId);
//        return product;
//    }

}
