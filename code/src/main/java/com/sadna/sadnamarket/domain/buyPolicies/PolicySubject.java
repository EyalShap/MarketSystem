package com.sadna.sadnamarket.domain.buyPolicies;

import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
@Entity
@Table(name = "policy_subject")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subject_type", discriminatorType = DiscriminatorType.STRING)
public abstract class PolicySubject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subject_id")
    private int subjectId;

    public PolicySubject() {}

    public abstract int subjectAmount(List<CartItemDTO> cart, Map<Integer, ProductDTO> products);
    public abstract boolean isSubject(ProductDTO product);
    public abstract String getSubject();
    public abstract String getDesc();
    public abstract int getProductId();
    public abstract boolean equals(Object other);
}
