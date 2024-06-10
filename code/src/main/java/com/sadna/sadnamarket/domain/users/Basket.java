package com.sadna.sadnamarket.domain.users;

import com.sadna.sadnamarket.service.Error;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Basket {
    private int storeId;
    private HashMap<Integer, Integer> products;
    private static final Logger logger = LogManager.getLogger(Basket.class);

    public Basket(int storeId) {
        logger.info("Entering Basket constructor with storeId={}", storeId);
        this.storeId = storeId;
        products = new HashMap<>();
        logger.info("Exiting Basket constructor");
    }

    public int getStoreId() {
        logger.info("Entering getStoreId");
        logger.info("Exiting getStoreId with result={}", storeId);
        return storeId;
    }

    public void addProduct(int productId, int amount) {
        logger.info("Entering addProduct with productId={} and amount={}", productId, amount);
        if (hasProduct(productId))
            throw new IllegalArgumentException(Error.makeBasketProductAlreadyExistsError());
        products.put(productId, amount);
        logger.info("Exiting addProduct");
    }

    public void removeProduct(int productId) {
        logger.info("Entering removeProduct with productId={}", productId);
        if (!hasProduct(productId))
            throw new IllegalArgumentException(Error.makeBasketProductDoesntExistError());
        products.remove(productId);
        logger.info("Exiting removeProduct");
    }

    private boolean hasProduct(int productId) {
        logger.info("Entering hasProduct with productId={}", productId);
        boolean result = products.containsKey(productId);
        logger.info("Exiting hasProduct with result={}", result);
        return result;
    }

    public void changeQuantity(int productId, int quantity) {
        logger.info("Entering changeQuantity with productId={} and quantity={}", productId, quantity);
        if (!hasProduct(productId))
            throw new IllegalArgumentException(Error.makeBasketProductDoesntExistError());
        products.replace(productId, quantity);
        logger.info("Exiting changeQuantity");
    }

    public HashMap<Integer,Integer> getProducts(){
        logger.info("return products from basket {}",products);
        return products;
    }
    public boolean isEmpty(){
        logger.info("check if basket empty");
        boolean isEmpty=products.isEmpty();
        logger.info("checked if basket empty and got: {}",isEmpty);
        return isEmpty;
    }
}
