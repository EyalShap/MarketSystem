package com.sadna.sadnamarket.domain.payment;

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
}
