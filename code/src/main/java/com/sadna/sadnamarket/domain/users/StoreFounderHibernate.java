package com.sadna.sadnamarket.domain.users;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STORE_FOUNDER")
public class StoreFounderHibernate extends StoreOwnerHibernate {
    
}
