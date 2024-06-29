package com.sadna.sadnamarket.domain.buyPolicies;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.LinkedList;

@Entity
@Table(name = "jewishbuypolicies")
public class JewishCustomsBuyPolicyDTO extends BuyPolicyDTO{
    @Column(name = "subject")
    String subject;
    @Column(name = "type")
    String type;

    public JewishCustomsBuyPolicyDTO(){

    }

    public JewishCustomsBuyPolicyDTO(Integer policyId, String subject, String type) {
        this.policyId = policyId;
        this.subject = subject;
        this.type = type;
    }

    public JewishCustomsBuyPolicyDTO(String subject, String type) {
        this.subject = subject;
        this.type = type;
    }

    @Override
    public Query getUniqueQuery(Session session) {
        Query query = session.createQuery("SELECT P FROM JewishCustomsBuyPolicyDTO P " +
                "WHERE P.type = :type " +
                "AND P.subject = :subject ");
        query.setParameter("type",type);
        query.setParameter("subject",subject);
        return query;
    }

    @Override
    public BuyPolicy toBuyPolicy() {
        PolicySubject policySubject;
        if(subject.startsWith("P")){
            policySubject = new ProductSubject(Integer.parseInt(subject.substring(2)));
        }else{
            policySubject = new CategorySubject(subject.substring(2));
        }
        switch (type){
            case BuyPolicyTypeCodes.HOLIDAY:
                return new HolidayBuyPolicy(policyId, new LinkedList<>(),policySubject);
            case BuyPolicyTypeCodes.ROSH_KHODESH:
                return new RoshChodeshBuyPolicy(policyId, new LinkedList<>(),policySubject);

        }
        return null;
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public int getId1() {
        return 0;
    }

    @Override
    public int getId2() {
        return 0;
    }

    @Override
    public BuyPolicy toBuyPolicy(BuyPolicy policy1, BuyPolicy policy2) {
        return null;
    }
}
