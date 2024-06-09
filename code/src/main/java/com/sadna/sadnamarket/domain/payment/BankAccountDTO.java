package com.sadna.sadnamarket.domain.payment;

import java.util.Objects;

public class BankAccountDTO {
    String bankCode;
    String bankBranchCode;
    String accountCode;
    String ownerId;

    public BankAccountDTO(String bankCode, String bankBranchCode, String accountCode, String ownerId) {
        this.bankCode = bankCode;
        this.bankBranchCode = bankBranchCode;
        this.accountCode = accountCode;
        this.ownerId = ownerId;
    }

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
