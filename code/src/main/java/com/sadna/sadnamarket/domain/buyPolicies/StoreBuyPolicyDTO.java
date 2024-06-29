package com.sadna.sadnamarket.domain.buyPolicies;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "storebuypolicies")
public class StoreBuyPolicyDTO implements Serializable {
    @Id
    @Column(name = "store")
    int storeId;

    @Id
    @Column(name = "policy")
    int policyId;

    @Column(name = "legal")
    boolean legal;

    public StoreBuyPolicyDTO(int storeId, int policyId, boolean legal) {
        this.storeId = storeId;
        this.policyId = policyId;
        this.legal = legal;
    }

    public StoreBuyPolicyDTO(){

    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getPolicyId() {
        return policyId;
    }

    public void setPolicyId(int policyId) {
        this.policyId = policyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreBuyPolicyDTO that = (StoreBuyPolicyDTO) o;
        return storeId == that.storeId && policyId == that.policyId && legal == that.legal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, policyId, legal);
    }
}
