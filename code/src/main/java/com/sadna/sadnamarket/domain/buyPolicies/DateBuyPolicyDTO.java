package com.sadna.sadnamarket.domain.buyPolicies;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.LinkedList;

@Entity
@Table(name = "datebuypolicies")
public class DateBuyPolicyDTO extends BuyPolicyDTO{
    @Column(name = "day")
    Integer day;
    @Column(name = "month")
    Integer month;
    @Column(name = "year")
    Integer year;
    @Column(name = "subject")
    String subject;

    public DateBuyPolicyDTO(){

    }

    public DateBuyPolicyDTO(Integer policyId, Integer day, Integer month, Integer year, String subject) {
        this.policyId = policyId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.subject = subject;
    }

    public DateBuyPolicyDTO(Integer day, Integer month, Integer year, String subject) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.subject = subject;
    }

    @Override
    public Query getUniqueQuery(Session session) {
        Query query = session.createQuery("SELECT P FROM DateBuyPolicyDTO P " +
                "WHERE P.day = :day " +
                "AND P.month = :month " +
                "AND P.year = :year " +
                "AND P.subject = :subject ");
        query.setParameter("day",day);
        query.setParameter("month",month);
        query.setParameter("year",year);
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
        return new SpecificDateBuyPolicy(policyId,new LinkedList<>(), policySubject, day, month, year);
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
