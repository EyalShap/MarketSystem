package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "andcondition")
public class AndCondition extends CompositeCondition{

    public AndCondition(int id, Condition conditionA, Condition conditionB) {
        super(id, conditionA, conditionB);
    }
    public AndCondition(Condition conditionA, Condition conditionB) {
        super(conditionA, conditionB);
    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice) {
        boolean condAisMet = conditionA.checkCond(productDTOMap, listProductsPrice);
        boolean condBisMet = conditionB.checkCond(productDTOMap, listProductsPrice);
        return condAisMet && condBisMet;
    }

    @Override
    public String description() {
        return conditionA.description() + " and " + conditionB.description();
    }


    @Override
    public Query getUniqueQuery(Session session) {
        Query query = session.createQuery("SELECT A FROM andcondition A " +
                "WHERE A.id1 = :id1 " +
                "AND A.id2 = :id2 " );
        query.setParameter("id1", conditionA.getId());
        query.setParameter("id2",conditionB.getId());
        return query;
    }
}
