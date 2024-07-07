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
//            ProductDTO productDTO=session.get(ProductDTO.class, productId);
//            return productDTOToProduct(productDTO);
            return session.get(Product.class, productId);
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
    public Set<Integer> getAllProductIds() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Integer> productIds = session.createQuery("SELECT p.productId FROM Product p", Integer.class).list();
            return new HashSet<>(productIds);
        } catch (Exception e) {
            throw new IllegalArgumentException(Error.makeDBError());
        }
    }

    @Override
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

   // public static void main(String[] args) {
      //  HibernateProductRepository a = new HibernateProductRepository();
      //  a.clean();
        //Product p=new Product();
        //p.setActive();
        //p.setStoreId();
        //p.
     //   int y=a.addProduct("banana",7,"fruit",4,2,4,"dsflkj");
     //   System.out.println(y);
//        a.addProduct("orange",10,"fruit",2,6,2,"aaaaa");
//        a.addProduct("cucumber",2,"vegetable",1,10,7,"bkalba");
       // a.addProduct("banana",4,"fruit",5,2,17,"dsflkj");

//        List<Integer> u=new ArrayList<>();
//        u.add(1116);
//        u.add(1118);
//        u.add(1148);
//        List<Product>t=a.getProducts(u);
        //List<Product>j=getTopProducts();
        //List<Product>h= a.filterByCategory("fruit");
        //List<Product> u =a.filterByName("banana");
       // a.removeProduct(1119);
       // boolean k=a.isExistProduct(1116);
        //List<Product>r=a.getAllProducts();
        //Set<Integer>k=a.getAllProductIds();
   //     int x=4;
   // }

}
