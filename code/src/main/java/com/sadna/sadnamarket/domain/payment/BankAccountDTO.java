package com.sadna.sadnamarket.domain.payment;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class BankAccountDTO {
    @Column(name = "bank_code")
    String bankCode;
    @Column(name = "bank_branch_code")
    String bankBranchCode;
    @Column(name = "account_code")
    String accountCode;
    @Column(name = "owner_username")
    String ownerId;

    public BankAccountDTO(String bankCode, String bankBranchCode, String accountCode, String ownerId) {
        this.bankCode = bankCode;
        this.bankBranchCode = bankBranchCode;
        this.accountCode = accountCode;
        this.ownerId = ownerId;
    }

    public BankAccountDTO() {}

    public String getBankCode() {
        return bankCode;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountDTO that = (BankAccountDTO) o;
        return Objects.equals(bankCode, that.bankCode) && Objects.equals(bankBranchCode, that.bankBranchCode) && Objects.equals(accountCode, that.accountCode) && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankCode, bankBranchCode, accountCode, ownerId);
    }
}
