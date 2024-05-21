package com.sadna.sadnamarket.domain.users;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class MemoryRepo implements IUserRepository {
    private static HashMap<String,Member> members;
    private static HashMap<Integer,Guest> guests;
    private int guestId=0;

    public MemoryRepo() {
        members=new HashMap<>();
    }

    @Override
    public Member getMember(String userName) {
       if(hasMember(userName)){
            return members.get(userName);
       }
       else return null;
      
    }

    public boolean hasMember(String username){
        if(members.containsKey(username))
            return true;
        else{ 
            throw new NoSuchElementException("user doesnt exist");
        }
    }

    @Override
    public List<Member> getAll() {
        return new ArrayList<>(members.values());
    }       

    @Override
    public void store(Member member) {
        members.put(member.getName(),member);
    }

    public int addGuest(){
        guests.put(guestId, new Guest(guestId));
        return guestId++;
    }
   
    public void deleteGuest(int guestID){
        guests.remove(guestID);
    }

     public boolean hasGuest(int guestID){
        if(guests.containsKey(guestID))
            return true;
        else{ 
            throw new NoSuchElementException("guest doesnt exist");
        }
    }

    @Override
    public Guest getGuest(int guestID) {
       if(hasGuest(guestID)){
            return guests.get(guestID);
       }
       else return null;
    }
   
}
