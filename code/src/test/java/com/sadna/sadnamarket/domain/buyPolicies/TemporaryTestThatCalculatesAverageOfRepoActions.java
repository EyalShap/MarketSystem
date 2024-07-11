package com.sadna.sadnamarket.domain.buyPolicies;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class TemporaryTestThatCalculatesAverageOfRepoActions {
    public static HibernateBuyPolicyRepository repo;
    public static BuyPolicyManager man;

    @BeforeAll
    public static void setup(){
        repo = new HibernateBuyPolicyRepository();
        man = repo.createManager(null,0);
        repo.clear();
    }

    @Test
    public void calculateFindPolicyTime() throws JsonProcessingException {
        int id = repo.addProductAmountBuyPolicy(0,new LinkedList<>(),0,100);
        man.addBuyPolicy(id);
        long sum = 0;
        for(int i = 0; i < 50; i++){
            long before = System.currentTimeMillis();
            man.hasPolicy(id);
            sum += System.currentTimeMillis()-before;
        }
        System.out.println(sum/50);
    }

    @Test
    public void calculateAddPolicyTime() throws JsonProcessingException {
        int id = repo.addProductAmountBuyPolicy(0,new LinkedList<>(),0,100);
        long sum = 0;
        for(int i = 0; i < 50; i++){
            man.removeBuyPolicy(id);
            long before = System.currentTimeMillis();
            man.addBuyPolicy(id);
            sum += System.currentTimeMillis()-before;
        }
        System.out.println(sum/50);
    }

    @Test
    public void calculateClearTime() throws JsonProcessingException {
        int id = repo.addProductAmountBuyPolicy(0,new LinkedList<>(),0,100);
        long sum = 0;
        for(int i = 0; i < 50; i++){
            long before = System.currentTimeMillis();
            man.removeBuyPolicy(id);
            sum += System.currentTimeMillis()-before;
            man.addBuyPolicy(id);
        }
        System.out.println(sum/50);
    }
}
