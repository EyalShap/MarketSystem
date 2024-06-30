package com.sadna.sadnamarket.domain.users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Id;

@Entity
@Table(name = "guests")
public class Guest extends IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int guestId;
    private static final Logger logger = LogManager.getLogger(MemoryRepo.class);

    public Guest(int guestId){
        logger.info("creating new guest with guest_id: {}",guestId);
        cart=new Cart();
        this.guestId=guestId;
        logger.info("done creating new guest with guest_id: {}",guestId);
    }
    public Guest(){
        logger.info("creating new guest");
        cart=new Cart();
        logger.info("done creating new guest");
    }

    @Override
    public boolean isLoggedIn() {
        logger.info("guest {} is login is {}",guestId,false);
        return false;
    }
   
    
}
