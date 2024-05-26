package com.sadna.sadnamarket.domain.users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class MemoryRepo implements IUserRepository {
    private static HashMap<String, Member> members;
    private static HashMap<Integer, Guest> guests;
    private int guestId = 0;
    private static final Logger logger = LogManager.getLogger(MemoryRepo.class);

    public MemoryRepo() {
        logger.info("Entering MemoryRepo constructor");
        members = new HashMap<>();
        guests = new HashMap<>();
        logger.info("Exiting MemoryRepo constructor");
    }

    @Override
    public Member getMember(String userName) {
        logger.info("Entering getMember with userName={}", userName);
        Member member = members.get(userName);
        if (member == null) {
            logger.error("Exception in getMember: User with userName={} does not exist", userName);
            throw new NoSuchElementException("User with userName " + userName + " does not exist");
        }
        logger.info("Exiting getMember with result={}", member);
        return member;
    }

    public boolean hasMember(String username) {
        logger.info("Entering hasMember with username={}", username);
        boolean result = members.containsKey(username);
        if (!result) {
            logger.error("Exception in hasMember: User with username={} does not exist", username);
            throw new NoSuchElementException("User with username " + username + " does not exist");
        }
        logger.info("Exiting hasMember with result={}", result);
        return result;
    }

    @Override
    public List<Member> getAll() {
        logger.info("Entering getAll");
        List<Member> allMembers = new ArrayList<>(members.values());
        logger.info("Exiting getAll with result={}", allMembers);
        return allMembers;
    }

    @Override
    public void store(Member member) {
        logger.info("Entering store with member={}", member);
        members.put(member.getUsername(), member);
        logger.info("Exiting store");
    }

    public int addGuest() {
        logger.info("Entering addGuest");
        guests.put(++guestId, new Guest(guestId));
        logger.info("Exiting addGuest with result={}", guestId);
        return guestId;
    }

    public void deleteGuest(int guestID) {
        logger.info("Entering deleteGuest with guestID={}", guestID);
        if (hasGuest(guestID)) {
            guests.remove(guestID);
        }
        logger.info("Exiting deleteGuest");
    }

    public boolean hasGuest(int guestID) {
        logger.info("Entering hasGuest with guestID={}", guestID);
        boolean result = guests.containsKey(guestID);
        if (!result) {
            logger.error("Exception in hasGuest: Guest with guestID={} does not exist", guestID);
            throw new NoSuchElementException("Guest with guestID " + guestID + " does not exist");
        }
        logger.info("Exiting hasGuest with result={}", result);
        return result;
    }

    @Override
    public Guest getGuest(int guestID) {
        logger.info("Entering getGuest with guestID={}", guestID);
        Guest guest = guests.get(guestID);
        if (guest == null) {
            logger.error("Exception in getGuest: Guest with guestID={} does not exist", guestID);
            throw new NoSuchElementException("Guest with guestID " + guestID + " does not exist");
        }
        logger.info("Exiting getGuest with result={}", guest);
        return guest;
    }

    @Override
    public List<CartItemDTO> getUserCart(String username) {
        logger.info("getting member cart for {}",username);
        List<CartItemDTO> cartItemDTOs=getMember(username).getCartItems();
        logger.info("got member cart for {}: {}",username,cartItemDTOs);
        return cartItemDTOs;
    }

    @Override
    public List<CartItemDTO> getGuestCart(int guestID) {
        logger.info("getting guest cart for {}",guestID);
        List<CartItemDTO> cartItemDTOs=getGuest(guestID).getCartItems();
        logger.info("got guest cart for {}: {}",guestID,cartItemDTOs);
        return cartItemDTOs;
    }
}
