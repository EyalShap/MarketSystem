package com.sadna.sadnamarket.domain.products;

import com.sadna.sadnamarket.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sadna.sadnamarket.service.Error;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HibernateProductRepository implements IProductRepository{
    @Override
    public Product getProduct(int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ProductDTO productDTO=session.get(ProductDTO.class, productId);
            return productDTOToProduct(productDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<Integer> getAllProductIds() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> productIds = session.createQuery("SELECT p.productID FROM ProductDTO p", Integer.class).list();
            return new HashSet<>(productIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<ProductDTO>productDTOS= session.createQuery("FROM ProductDTO", ProductDTO.class).list();
            for (ProductDTO productDTO : productDTOS) {
                products.add(productDTOToProduct(productDTO));
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> getProducts(List<Integer> productIds) {
        List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ProductDTO> query = session.createQuery(
                    "FROM ProductDTO p WHERE p.productID IN (:productIds)", ProductDTO.class);
            query.setParameterList("productIds", productIds);
            List<ProductDTO> productDTOs = query.list();
            for (ProductDTO productDTO : productDTOs) {
                products.add(productDTOToProduct(productDTO));
            }
            return products;
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void removeProduct(int productId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ProductDTO product = session.get(ProductDTO.class, productId);
            if (product != null) {
                session.delete(product);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public int addProduct(String productName, double productPrice, String productCategory, double productRank, double productWeight, int storeId) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ProductDTO product=new ProductDTO();

            product.setProductName(productName);
            product.setProductPrice(productPrice);
            product.setProductCategory(productCategory);
            product.setProductRank(productRank);
            product.setProductWeight(productWeight);
            product.setStoreId(storeId);
            product.setActive(true);

            session.save(product);
            transaction.commit();
            return product.getStoreId();
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Constraint violation: " + e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error saving product: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean isExistProduct(int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(p) FROM ProductDTO p WHERE p.productID = :productId", Long.class);
            query.setParameter("productId", productId);

            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> filterByName(String productName) {
        List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM ProductDTO p WHERE p.productName LIKE :productName";
            List<ProductDTO> productDTOS=new ArrayList<>();
            productDTOS= session.createQuery(hql, ProductDTO.class)
                    .setParameter("productName", "%" + productName + "%")
                    .list();
            for (ProductDTO productDTO:productDTOS) {
                products.add(productDTOToProduct(productDTO));
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> filterByCategory(String category) {
        List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM ProductDTO p WHERE p.productCategory = :category";
            List<ProductDTO> productDTOS=new ArrayList<>();
            productDTOS= session.createQuery(hql, ProductDTO.class)
                    .setParameter("category", category)
                    .list();
            for (ProductDTO productDTO:productDTOS) {
                products.add(productDTOToProduct(productDTO));
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> getTopProducts() {
        List<Product> products=new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM ProductDTO p ORDER BY p.productRank DESC";
            List<ProductDTO> productDTOS= session.createQuery(hql, ProductDTO.class).list();
            for (ProductDTO productDTO:productDTOS) {
                products.add(productDTOToProduct(productDTO));
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Product productDTOToProduct(ProductDTO productDTO){
        int id=productDTO.getProductID();
        String productName=productDTO.getProductName();
        double productPrice = productDTO.getProductPrice();
        String productCategory=productDTO.getProductCategory();
        double productRank=productDTO.getProductRank();
        double productWeight=productDTO.getProductWeight();
        int storeId=productDTO.getStoreId();
        Product product=new Product(id,productName,productPrice,productCategory,productRank,productWeight,storeId);
        return product;
    }

}
