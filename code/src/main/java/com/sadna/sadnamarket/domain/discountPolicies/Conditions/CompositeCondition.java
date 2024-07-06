package com.sadna.sadnamarket.domain.discountPolicies.Conditions;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CompositeCondition extends Condition{
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conditionA_id", referencedColumnName = "id")
    protected Condition conditionA;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conditionB_id", referencedColumnName = "id")
    protected Condition conditionB;

    public CompositeCondition(int id, Condition conditionA, Condition conditionB){
        super(id);
        this.conditionA = conditionA;
        this.conditionB = conditionB;

    }
    public CompositeCondition(Condition conditionA, Condition conditionB){
        this.conditionA = conditionA;
        this.conditionB = conditionB;
    }
    @Override
    public abstract boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice);
    @Override
    public abstract Query getUniqueQuery(Session session);
}
