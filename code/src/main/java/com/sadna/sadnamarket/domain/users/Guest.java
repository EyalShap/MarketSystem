package com.sadna.sadnamarket.domain.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Guest extends IUser {
    int guestId;
    private static final Logger logger = LogManager.getLogger(MemoryRepo.class);

    public Guest(int guestId){
        logger.info("creating new guest with guest_id: {}",guestId);
        cart=new Cart();
        this.guestId=guestId;
        logger.info("done creating new guest with guest_id: {}",guestId);
    }


    @Override
    public boolean isLoggedIn() {
        logger.info("guest {} is login is {}",guestId,false);
        return false;
    }
   
    
}
