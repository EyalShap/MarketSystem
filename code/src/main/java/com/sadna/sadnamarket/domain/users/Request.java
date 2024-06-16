package com.sadna.sadnamarket.domain.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Request extends Notification {
    private String senderName;
    private int storeId;
    private String role;
    private static final Logger logger = LogManager.getLogger(Request.class);

    public Request(String senderName, String msg, int storeId, String role,int id) {
        super(msg,id);
        logger.info("Entering Request constructor with senderName={}, msg={}, storeId={}, role={}", senderName, msg, storeId, role);
        this.senderName = senderName;
        this.storeId = storeId;
        this.role = role;
        logger.info("Exiting Request constructor");
    }

    private void acceptOwner(Member accepting) {
        logger.info("Entering acceptOwner with accepting={}", accepting);
        accepting.addRole(new StoreOwner(storeId, senderName));
        logger.info("Exiting acceptOwner");
    }

    private void acceptManager(Member accepting) {
        logger.info("Entering acceptManager with accepting={}", accepting);
        accepting.addRole(new StoreManager(storeId));
        logger.info("Exiting acceptManager");
    }

    public void accept(Member accepting) {
        logger.info("Entering accept with accepting={}", accepting);
        if (role.equals("Manager"))
            acceptManager(accepting);
        else
            acceptOwner(accepting);
        logger.info("Exiting accept");
    }

    public int getStoreId() {
        logger.info("Entering getStoreId");
        logger.info("Exiting getStoreId with result={}", storeId);
        return storeId;
    }

    public String getRole() {
        logger.info("Entering getRole");
        logger.info("Exiting getRole with result={}", role);
        return role;
    }

    public String getSender() {
        logger.info("Entering getSender");
        logger.info("Exiting getSender with result={}", senderName);
        return senderName;
    }

    @Override
    public NotificationDTO toDTO(){
        return new RequestDTO(this);
    }

    @Override
    public String toString() {
        logger.info("Entering toString");
        String result = super.toString() + " from store " + storeId + " from user: " + senderName + " as " + role;
        logger.info("Exiting toString with result={}", result);
        return result;
    }
}
