package com.sadna.sadnamarket.api;

public class PolicyConditionRequest {
    int conditionAID;
    int conditionBID;

    double percentage;
    String CategoryName;

    String ProductName;
    public int getConditionAID() {
        return conditionAID;
    }

    public void setConditionAID(int conditionAID) {
        this.conditionAID = conditionAID;
    }

    public int getConditionBID() {
        return conditionBID;
    }

    public void setConditionBID(int conditionBID) {
        this.conditionBID = conditionBID;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }
}
