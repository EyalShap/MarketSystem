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
@Table(name = "trueCondition")
public class TrueCondition extends Condition{

    public TrueCondition(int id) {
        super(id);
    }
    public TrueCondition() {
        super();
    }

    @Override
    public boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice) {
        return true;
    }

    @Override
    public String description() {
        return "true";
    }

    @Override
    public Query getUniqueQuery(Session session) {
        Query query = session.createQuery("SELECT A FROM trueCondition A ");
        return query;
    }
}
