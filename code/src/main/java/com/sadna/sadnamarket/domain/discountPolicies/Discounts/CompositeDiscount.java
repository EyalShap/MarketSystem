package com.sadna.sadnamarket.domain.discountPolicies.Discounts;

import com.sadna.sadnamarket.domain.discountPolicies.ProductDataPrice;
import com.sadna.sadnamarket.domain.products.ProductDTO;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CompositeDiscount extends Discount {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discountA_id", referencedColumnName = "id")
    protected Discount discountA;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discountB_id", referencedColumnName = "id")
    protected Discount discountB;
    public CompositeDiscount(int id, Discount discountA, Discount discountB){
        super(id);
        this.discountA = discountA;
        this.discountB = discountB;
    }
    public CompositeDiscount(Discount discountA, Discount discountB){
        this.discountA = discountA;
        this.discountB = discountB;
    }

    @Override
    public abstract void giveDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice);

    @Override
    public abstract boolean checkCond(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> listProductsPrice);
    @Override
    public abstract double giveTotalPriceDiscount(Map<Integer, ProductDTO> productDTOMap, List<ProductDataPrice> ListProductsPrice);
    
}
